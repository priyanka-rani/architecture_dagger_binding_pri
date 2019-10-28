package com.pri.architecture_boilerplate.api.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseResponse : Serializable{
    @SerializedName("statusCode",alternate = ["StatusCode", "status", "Status","responseCode"])
    @Expose
    var statusCode: Int? = null
    @SerializedName(value = "message", alternate = ["Error","error","Message", "title","responseDescription"])
    @Expose
    var message: String? = null
}