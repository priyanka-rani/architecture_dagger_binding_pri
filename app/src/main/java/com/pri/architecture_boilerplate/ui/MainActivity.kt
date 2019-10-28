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

package com.pri.architecture_boilerplate.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pri.architecture_boilerplate.R
import com.pri.architecture_boilerplate.databinding.MainActivityBinding
import com.pri.architecture_boilerplate.event.ErrorMessageEvent
import com.pri.architecture_boilerplate.event.LoadingEvent
import com.pri.architecture_boilerplate.prefs.PreferencesHelper
import com.pri.architecture_boilerplate.util.CBProgressDialog
import com.pri.architecture_boilerplate.util.CommonUtils
import com.pri.architecture_boilerplate.util.NetworkUtils
import dagger.android.support.DaggerAppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


open class MainActivity : DaggerAppCompatActivity(){
    private var pDialog: CBProgressDialog? = null

    lateinit var binding: MainActivityBinding

    private val navController: NavController
        get() = findNavController(R.id.container)


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    @Inject
    lateinit var preferencesHelper: PreferencesHelper


    companion object {
        const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        pDialog = CBProgressDialog(this)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.main_activity)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
    }



    private fun showErrorSnack(message: String) {
        CommonUtils.showErrorSnack(binding.root, message)
    }


    private fun showProgressDialog() {
        pDialog?.showDialog()
    }

    private fun dismissProgressDialog() {
        pDialog?.hideDialog()
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorMessageEvent(event: ErrorMessageEvent) {
        val message: String
        if (NetworkUtils.isNetworkConnected(this@MainActivity)) {
            if (event.message.isNotBlank()) {
                    showErrorSnack(event.message)
            }
        } else {
            showErrorSnack(getString(R.string.no_internet_error))
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadingEvent(event: LoadingEvent) {
        if (event.loading) {
            showProgressDialog()
            CommonUtils.dismissKeyboard(this@MainActivity)
        } else dismissProgressDialog()
    }


    /**
     * Dismiss all DialogFragments added to given FragmentManager and child fragments
     */
    private fun dismissAllDialogs(fragmentManager: FragmentManager) {
        for (fragment in fragmentManager.fragments) {
            if (fragment is DialogFragment) {
                fragment.dismissAllowingStateLoss()
            }

            val childFragmentManager = fragment.childFragmentManager
            dismissAllDialogs(childFragmentManager)
        }
    }
}

