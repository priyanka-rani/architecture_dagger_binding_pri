package com.pri.architecture_boilerplate.api.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ErrorResponse : BaseResponse() {
    @SerializedName("details", alternate = ["Details", "error_description"])
    @Expose
    var details: String? = null

    override fun toString(): String {
        return if (details.isNullOrBlank())
            "$message"
        else "$details" +
                if (/*BuildConfig.DEBUG && */statusCode!= null) " ($statusCode)"/*show status code in debug build only*/
                else ""
    }
}