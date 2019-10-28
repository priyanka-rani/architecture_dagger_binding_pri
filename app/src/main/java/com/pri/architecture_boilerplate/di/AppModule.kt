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

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.pri.architecture_boilerplate.BuildConfig
import com.pri.architecture_boilerplate.db.*
import com.pri.architecture_boilerplate.prefs.AppPreferencesHelper
import com.pri.architecture_boilerplate.prefs.PreferencesHelper
import com.pri.architecture_boilerplate.util.AppConstants
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module(
        includes = [
            ViewModelModule::class,
            NetworkModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
                .databaseBuilder(app, AppDb::class.java, "cashbaba.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun providePicasso(app: Application, okHttpClient: OkHttpClient) = Picasso.Builder(app)
            .downloader(OkHttp3Downloader(okHttpClient))
            .listener { _, _, e -> if (BuildConfig.DEBUG) e.printStackTrace() }
            .loggingEnabled(BuildConfig.DEBUG)
            .build()

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDb): MovieDao {
        return db.movieDao()
    }


    @Singleton
    @Provides
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }
}
