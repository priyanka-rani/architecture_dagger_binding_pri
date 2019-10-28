package com.pri.architecture_boilerplate.util

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.pri.architecture_boilerplate.R
import java.util.regex.Pattern

/**
 * Created by Priyanka on 11/3/19.
 */

interface Validator {
    val errorMsg: Int
    fun validate(any: CharSequence?): Boolean
}

val UserNameValidator = object : Validator {
    var any: CharSequence? = null
    override val errorMsg: Int
        get() = any?.toString().userNameError() ?: R.string.error_invalid_email

    override fun validate(any: CharSequence?): Boolean {
        this.any = any
        return MobileValidator.validate(any) || EmailValidator.validate(any)
    }
}

val EmailValidator = object : Validator {
    override val errorMsg: Int
        get() = R.string.error_invalid_email

    override fun validate(any: CharSequence?): Boolean =
            !any.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(any.trim()).matches()
}


val NameValidator = object : Validator {
    private val NAME_PATTERN = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z.\\s]{0,}$"
    override val errorMsg: Int
        get() = R.string.error_invalid_name

    override fun validate(any: CharSequence?): Boolean = !any.isNullOrBlank() && Pattern
            .compile(NAME_PATTERN).matcher(any).matches() && any.length in 4..64

}


val MobileValidator = object : Validator {
    override val errorMsg: Int
        get() = R.string.error_invalid_mobile

    override fun validate(any: CharSequence?): Boolean =
            !any.isNullOrBlank() && isValidMobile(any)
}


fun isValidMobile(any: CharSequence): Boolean {
    var phoneNumber = any
    if (Patterns.PHONE.matcher(phoneNumber).matches()) {
        if (phoneNumber.length > 10 && phoneNumber.length < 15) {
            if (phoneNumber.length == 13) {
                if (phoneNumber.startsWith("88"))
                    phoneNumber = phoneNumber.substring(2)
                else
                    return false
            } else if (phoneNumber.length == 14) {
                if (phoneNumber.startsWith("+88"))
                    phoneNumber = phoneNumber.substring(3)
                else
                    return false
            }
            if (phoneNumber.length == 11) {
                when (phoneNumber.substring(0, 3)) {
                    "011", "013", "014", "015", "016", "017", "018", "019" -> return true
                }
            }
        }
        return false
    }
    return false
}


