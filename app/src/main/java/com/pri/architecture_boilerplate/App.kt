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

package com.pri.architecture_boilerplate

import android.app.Service
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.pri.architecture_boilerplate.binding.FragmentDataBindingComponent
import com.pri.architecture_boilerplate.di.DaggerAppComponent
import com.pri.architecture_boilerplate.event.ForegroundEvent
import com.pri.architecture_boilerplate.prefs.PreferencesHelper
import com.squareup.picasso.Picasso
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerApplication
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject


class App : DaggerApplication(), LifecycleObserver {
    // Add this line
    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    private val applicationInjector = DaggerAppComponent.builder()
            .application(this)
            .build()

    override fun applicationInjector() = applicationInjector

    // override this method after implementing HasServiceInjector

    private var screenOff = false

    override fun onCreate() {
        super.onCreate()

        // Inject this class's @Inject-annotated members.
        applicationInjector.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


        DataBindingUtil.setDefaultComponent(FragmentDataBindingComponent())

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Log.d("Awww", "App in background")
        if (preferencesHelper.isLoggedIn && !screenOff) {
            /*enable pin lock*/
            preferencesHelper.setLastActiveMillis()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("Yeeey", "App in foreground")
        EventBus.getDefault().post(ForegroundEvent())
    }


    companion object {
        private const val TAG = "AppData"
    }
}
