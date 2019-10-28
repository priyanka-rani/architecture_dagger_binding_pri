/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.pri.architecture_boilerplate.prefs

/**
 * Created by amitshekhar on 07/07/17.
 */

interface PreferencesHelper {

    var accessToken: String?

    var tokenType: String?

    var accessTokenExpiresIn: Long

    var refreshToken: String?

    var userName: String?

    var userId: Int

    var userType: String?

    var email: String?

    var mobile: String?

    var countryCode: String?

    var currencySymbol: String?

    var currencyCode: String?

    var dateFormat: String?

    var publicKey: String?

    var otpExpiresIn: String?

    var fcmToken: String?

    var balance: String?

    var isLoggedIn: Boolean

    var isTapTargetShow: Boolean


    var txRateLimits: MutableMap<String, Long>?

    val isAccessTokenExpired: Boolean

    var favList: List<String>

    var lastActiveMillis: Long

    fun setLastActiveMillis()

    fun getString(key: String, defValue: String): String?

    fun putString(key: String, value: String)

    fun getBoolean(key: String, defValue: Boolean): Boolean?

    fun putBoolean(key: String, value: Boolean)

    fun getInt(key: String, defValue: Int): Int?

    fun putInt(key: String, value: Int)

    fun logoutUser()

}
