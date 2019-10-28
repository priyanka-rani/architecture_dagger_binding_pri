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

package com.pri.architecture_boilerplate.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pri.architecture_boilerplate.BuildConfig
import com.pri.architecture_boilerplate.api.API_URL
import com.pri.architecture_boilerplate.api.ApiService
import com.pri.architecture_boilerplate.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE

        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)

        /*if (BuildConfig.DEBUG) setUnsafeSslFactoryForClient(clientBuilder)  *//* Disabled TLS for development *//*
          else*/
       /* setSafeSslFactoryForClient(clientBuilder)//Enabled TLS for Production*/

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson =
            GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
            GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideLiveDataCallAdapterFactory(): LiveDataCallAdapterFactory = LiveDataCallAdapterFactory()

    @Provides
    @Singleton
    fun provideNullOrEmptyConverterFactory(): Converter.Factory =
            object : Converter.Factory() {
                override fun responseBodyConverter(
                        type: Type,
                        annotations: Array<out Annotation>,
                        retrofit: Retrofit
                ): Converter<ResponseBody, Any?> {
                    val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
                            this,
                            type,
                            annotations
                    )

                    return Converter { body: ResponseBody ->
                        if (body.contentLength() == 0L) null
                        else nextResponseBodyConverter.convert(body)
                    }
                }
            }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
            liveDataCallAdapterFactory: LiveDataCallAdapterFactory,
            nullOrEmptyConverterFactory: Converter.Factory,
            gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder =
            Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addConverterFactory(nullOrEmptyConverterFactory)
                    .addCallAdapterFactory(liveDataCallAdapterFactory)



    /* Configurations For Api which doesn't require authentication */

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient, retrofitBuilder: Retrofit.Builder):
            ApiService {
        return retrofitBuilder
                .client(okHttpClient).build()
                .create(ApiService::class.java)
    }


}
