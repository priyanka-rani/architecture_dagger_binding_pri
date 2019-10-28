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

import androidx.lifecycle.LiveData
import com.pri.architecture_boilerplate.AppExecutors
import com.pri.architecture_boilerplate.api.ApiService
import com.pri.architecture_boilerplate.api.model.MovieListResponseModel
import com.pri.architecture_boilerplate.db.AppDb
import com.pri.architecture_boilerplate.prefs.PreferencesHelper
import com.pri.architecture_boilerplate.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */

@Singleton
class AuthRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        val apiService: ApiService,
        private val appDb: AppDb,
        private val preferencesHelper: PreferencesHelper
) {


    fun getMovieList(path: String, page:Int): LiveData<Resource<MovieListResponseModel>> {
        return object : NetworkOnlyResource<MovieListResponseModel>(appExecutors) {
            override fun createCall() = apiService.getMovieList(path, page)
            override val showProgress: Boolean
                get() = page==1
        }.asLiveData()
    }
    fun searchMovieList(query: String): LiveData<Resource<MovieListResponseModel>> {
        return object : NetworkOnlyResource<MovieListResponseModel>(appExecutors) {
            override fun createCall() = apiService.searchMovieList(query)
        }.asLiveData()
    }

}
