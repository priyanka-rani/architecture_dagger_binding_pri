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

package com.pri.architecture_boilerplate.ui.common

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.pri.architecture_boilerplate.AppExecutors
import com.pri.architecture_boilerplate.prefs.PreferencesHelper
import com.pri.architecture_boilerplate.util.CommonUtils
import com.pri.architecture_boilerplate.util.NetworkUtils
import com.pri.architecture_boilerplate.util.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject


abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors

    var viewDataBinding by autoCleared<T>()

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(context)

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
                DataBindingUtil.inflate(inflater, layoutId, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        viewDataBinding.executePendingBindings()

    }


    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()


    protected fun showToast(msg: String) {
        try {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    protected fun showSnack(msg: String) {
        try {
            CommonUtils.showSnack(viewDataBinding.root, msg)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    protected fun showSnack(msg: Int) {
        try {
            CommonUtils.showSnack(viewDataBinding.root, msg)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <T> LiveData<T>.observeTillNotNull(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            if (t != null)
                removeObserver(this)
        }
    })
}
