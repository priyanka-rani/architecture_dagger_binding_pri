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

package com.pri.architecture_boilerplate.api

import androidx.lifecycle.LiveData
import com.pri.architecture_boilerplate.api.ApiEndPoint.ENDPOINT_GET_MOVIE_LIST
import com.pri.architecture_boilerplate.api.ApiEndPoint.ENDPOINT_SEARCH_MOVIE_LIST
import com.pri.architecture_boilerplate.api.model.MovieListResponseModel
import retrofit2.http.*

/**
 * REST API access points which need no authentication header
 */
interface ApiService {
    // API to fetch movie list
    @GET(ENDPOINT_GET_MOVIE_LIST)
    fun getMovieList(
            @Path("path") path: String,
            @Query("page") page: Int
    ): LiveData<ApiResponse<MovieListResponseModel>>

    // API to search in movie list
    @GET(ENDPOINT_SEARCH_MOVIE_LIST)
    fun searchMovieList(
            @Query("query") query: String
    ): LiveData<ApiResponse<MovieListResponseModel>>


}
