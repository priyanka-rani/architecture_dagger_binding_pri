package com.pri.architecture_boilerplate.util

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.pri.architecture_boilerplate.R

/**
 * Created by Priyanka on 2019-06-09.
 */

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            afterTextChanged.invoke(p0.toString())
        }

        override fun afterTextChanged(editable: Editable?) {
        }
    })
}

fun List<View>.isValid(): Boolean {
    var isValid = true
    this.reversed().forEach { if (!it.isValid()) isValid = false }
    return isValid
}


fun View.isValid(): Boolean {
    val valid = if (this.visibility == View.VISIBLE) {
        when (this) {
            is TextInputLayout -> {
                when {
                    this.editText?.text?.isBlank() == true -> {
                        this.error = this.context.getString(R.string.empty_field)
                        false
                    }
                    else -> this.error.isNullOrBlank()
                }
            }
            is RadioGroup -> if (this.checkedRadioButtonId != -1) true
            else {
                val lastChildPos = this.childCount - 1
                (this.getChildAt(lastChildPos) as RadioButton).error = "Please select option"
                false
            }
            is Spinner ->
                if (this.selectedItem != null && !TextUtils.isEmpty(this.selectedItem.toString())) true
                else {
                    (this.selectedView as TextView).error = "Please ${this.adapter.getItem(0)}"
                    false
                }
            is ImageView -> {
                this.drawable != null
            }

            else -> false
        }
    } else {
        true
    }
    if (!valid) this.requestFocus()
    return valid
}


fun String?.userNameError() = when {
    MobileValidator.validate(this) || EmailValidator.validate(this) -> null
    android.util.Patterns.PHONE.matcher(this).matches() -> R.string.error_invalid_mobile
    else -> R.string.error_invalid_email
}