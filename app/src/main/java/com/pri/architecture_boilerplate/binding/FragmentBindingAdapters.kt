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

package com.pri.architecture_boilerplate.binding

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.pri.architecture_boilerplate.R
import com.pri.architecture_boilerplate.util.Validator
import com.pri.architecture_boilerplate.util.afterTextChanged
import com.pri.architecture_boilerplate.util.isValid

/**
 * Binding adapters that work with a fragment instance.
 */

class FragmentBindingAdapters {
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter("visibleInvisible")
    fun showInvisible(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }


    @BindingAdapter("txtColor")
    fun bindTextColor(textView: TextView, res: Int?) {
        res?.let { textView.setTextColor(ContextCompat.getColor(textView.context, it)) }
    }

    @BindingAdapter("textToFormat", "android:text")
    fun setFormattedValue(view: TextView, textToFormat: Int, value: String) {
        view.text = String.format(view.context.resources.getString(textToFormat), value)
    }

    @BindingAdapter("validation", "errorMsg", requireAll = false)
    fun bindValidation(textInputLayout: TextInputLayout, validator: Validator?, errorMsg: Int?) {
        textInputLayout.editText?.afterTextChanged {
            textInputLayout.error = when {
                validator == null || validator.validate(it) -> null
                else -> when {
                    it.isBlank() -> null
                    else -> textInputLayout.context.getString(errorMsg
                            ?: validator.errorMsg)
                }

            }
        }
    }

    @BindingAdapter("android:longClickable")
    fun disableCopyPaste(editText: EditText, enableCopyPaste: Boolean) {
        if (!enableCopyPaste) {
            val actionModeCallBack = object : ActionMode.Callback {

                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode) {}

                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return false
                }
            }
            editText.customSelectionActionModeCallback = actionModeCallBack
            editText.setTextIsSelectable(false)
            editText.isLongClickable = false

            /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              editText.customInsertionActionModeCallback = actionModeCallBack
          }*/
        }
    }




    @BindingAdapter("android:onClick", "listOfTil")
    fun bindValidationToButton(button: Button, clickListener: View.OnClickListener, textFields: List<View>?) {
        button.setOnClickListener {
            if (textFields.isNullOrEmpty() || textFields.isValid()) {
                clickListener.onClick(button)
            }
        }
    }

}
