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

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pri.architecture_boilerplate.AppExecutors
import com.pri.architecture_boilerplate.util.AbsentLiveData


/**
 * Created by Priyanka on 24/3/19.
 */
abstract class NetworkOnlyResource<RequestType>
@MainThread constructor(appExecutors: AppExecutors) : NetworkBoundResource<RequestType, RequestType>(appExecutors) {
    private val interMediateResponse = MutableLiveData<RequestType>(null)
    @MainThread
    final override fun loadFromDb(): LiveData<RequestType> = interMediateResponse?:AbsentLiveData.create()

    @WorkerThread
    final override fun saveCallResult(item: RequestType) {
        interMediateResponse.postValue(item)
    }
}