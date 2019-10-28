/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pri.architecture_boilerplate.repository

import android.os.Handler
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pri.architecture_boilerplate.AppExecutors
import com.pri.architecture_boilerplate.api.*
import com.pri.architecture_boilerplate.event.LoadingEvent
import com.pri.architecture_boilerplate.util.CommonUtils
import com.pri.architecture_boilerplate.vo.Resource
import com.pri.architecture_boilerplate.vo.Status
import org.greenrobot.eventbus.EventBus

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {
    protected val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        Handler().post {
            /*show progress if set by api call and if no previous data is found*/
            if (showProgress)
                EventBus.getDefault().post(LoadingEvent(true))
        }
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    protected fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
            when (newValue.status) {
                Status.LOADING -> {
                    /*show progress if set by api call and if no previous data is found*/
                    Handler().post {
                        if (showProgress && newValue.data == null)
                            EventBus.getDefault().post(LoadingEvent(true))
                    }
                }
                Status.ERROR -> {
                    if (showProgress) EventBus.getDefault().post(LoadingEvent(false))
                    if (showError) newValue.message?.let {
                        CommonUtils.fireErrorMessageEvent(it, showInAlert = newValue.error?.statusCode
                                !in listOf(
                                ResponseCodes.CODE_UNAUTHORIZED,
                                ResponseCodes.CODE_TOKEN_EXPIRE
                        ))

                    }
                }
                Status.SUCCESS -> if (showProgress) EventBus.getDefault().post(LoadingEvent(false))
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        processResponse(response)?.let {
                            saveCallResult(it)
                            appExecutors.mainThread().execute {
                                // we specially request a new live data,
                                // otherwise we will get immediately last cached value,
                                // which may not be updated with latest results received from network.
                                result.addSource(loadFromDb()) { newData ->
                                    setValue(Resource.success(newData, response.message))
                                }
                            }
                        } ?: run {
                            appExecutors.mainThread().execute {
                                result.addSource(loadFromDb()) { newData ->
                                    setValue(Resource.error(newData, response.message))
                                }
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(newData, response.message, response.body))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    protected open val showProgress: Boolean = true

    protected open val showError: Boolean = true
}
