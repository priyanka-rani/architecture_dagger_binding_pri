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

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.pri.architecture_boilerplate.api.model.responsemodel.BaseResponse
import com.pri.architecture_boilerplate.api.model.responsemodel.ErrorResponse
import com.pri.architecture_boilerplate.util.CommonUtils
import retrofit2.Response
import java.io.IOException

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            var msg = error.message ?: "unknown error"
            if (error is IOException) {
                msg = "No Connectivity"
                CommonUtils.fireErrorMessageEvent(msg, false)
            }

            return ApiErrorResponse(msg)
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    val msg = if (body is BaseResponse) body.message else response.message()
                    ApiSuccessResponse(body = body, message = msg)
                    /*when (body) {
                        is TransactionResponse -> when {
                            body.responseCode.toString().endsWith("200", true) -> ApiSuccessResponse<T>(
                                body,
                                body.message
                            )
                            else -> {
                                val errorResponse = ErrorResponse()
                                errorResponse.message = body.message
                                errorResponse.statusCode = body.responseCode
                                ApiErrorResponse<T>(errorResponse, errorResponse.message)
                            }
                        }
                        else -> ApiSuccessResponse(body, message = msg)
                    }*/
                }
            } else {
                val gson = Gson()
                /* Logout if unauthorized */
                val msg: String

                if (response.code() == 504) {
                    msg = "No Connectivity"
                    ApiErrorResponse(msg)
                } else {
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = try {
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    } catch (e: JsonSyntaxException) {
                        null
                    }
                    msg = errorResponse?.toString() ?: response.message() ?: "unknown error"
                    if (errorResponse?.statusCode == null) errorResponse?.statusCode = response.code()
                    ApiErrorResponse(msg, errorResponse)
                }
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T?, val message: String? = null) : ApiResponse<T>()

data class ApiErrorResponse<T>(val message: String?, val body: ErrorResponse? = null) :
        ApiResponse<T>()
