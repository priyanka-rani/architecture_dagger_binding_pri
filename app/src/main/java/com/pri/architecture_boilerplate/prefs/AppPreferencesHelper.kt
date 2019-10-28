package com.pri.architecture_boilerplate.prefs


import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pri.architecture_boilerplate.di.PreferenceInfo
import com.pri.architecture_boilerplate.util.AppConstants
import javax.inject.Inject


class AppPreferencesHelper @Inject
constructor(context: Context, @PreferenceInfo prefFileName: String) : PreferencesHelper {
    private val mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override var accessToken
        @Synchronized
        get() = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        @Synchronized
        set(accessToken) {
            mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).commit()
        }

    override var tokenType
        get() = mPrefs.getString(PREF_KEY_TOKEN_TYPE, null)
        set(tokenType) = mPrefs.edit().putString(PREF_KEY_TOKEN_TYPE, tokenType).apply()


    override var accessTokenExpiresIn
        get() = mPrefs.getLong(PREF_KEY_ACCESS_TOKEN_EXPIRES_IN, 0)
        set(value) = mPrefs.edit().putLong(
                PREF_KEY_ACCESS_TOKEN_EXPIRES_IN,
                value
        ).apply()

    override var refreshToken
        @Synchronized
        get() = mPrefs.getString(PREF_KEY_REFRESH_TOKEN, null)
        @Synchronized
        set(refreshToken) {
            mPrefs.edit().putString(PREF_KEY_REFRESH_TOKEN, refreshToken).commit()
        }


    override var userName
        get() = mPrefs.getString(PREF_KEY_USER_NAME, null)
        @SuppressLint("ApplySharedPref")
        set(userName) {
            mPrefs.edit().putString(PREF_KEY_USER_NAME, userName).commit()
        }
    override var userType
        get() = mPrefs.getString(KEY_USER_TYPE, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            mPrefs.edit().putString(KEY_USER_TYPE, value).commit()
        }

    override var isLoggedIn: Boolean
        get() = mPrefs.getBoolean(PREF_KEY_IS_LOGGED_IN, false)
        @SuppressLint("ApplySharedPref")
        set(value) {
            mPrefs.edit().putBoolean(
                    PREF_KEY_IS_LOGGED_IN,
                    value
            ).commit()
        }

    override var isTapTargetShow: Boolean
        get() = mPrefs.getBoolean(TAP_TARGET_SHOW_KEY, false)
        set(value) = mPrefs.edit().putBoolean(
                TAP_TARGET_SHOW_KEY,
                value
        ).apply()


    override var userId
        get() = mPrefs.getInt(KEY_USER_ID, -1)
        set(value) = mPrefs.edit().putInt(KEY_USER_ID, value).apply()

    override var email
        get() = mPrefs.getString(KEY_EMAIL, null)
        set(value) {
            mPrefs.edit().putString(KEY_EMAIL, value).commit()
        }

    override var mobile
        get() = mPrefs.getString(KEY_MOBILE, null)
        set(value) {
            mPrefs.edit().putString(KEY_MOBILE, value).apply()
        }


    override var dateFormat
        get() = mPrefs.getString(KEY_DATE_FORMAT, null)
        set(value) = mPrefs.edit().putString(KEY_DATE_FORMAT, value).apply()

    override var countryCode
        get() = mPrefs.getString(KEY_COUNTRY_CODE, null)
        set(value) = mPrefs.edit().putString(KEY_COUNTRY_CODE, value).apply()

    override var currencyCode
        get() = mPrefs.getString(KEY_CURRENCY_CODE, AppConstants.COUNTRY_CODE)
        set(value) = mPrefs.edit().putString(KEY_CURRENCY_CODE, value).apply()

    override var currencySymbol
        get() = mPrefs.getString(KEY_CURRENCY_SYMBOL, null)
        set(value) = mPrefs.edit().putString(KEY_CURRENCY_SYMBOL, value).apply()

    override var publicKey
        get() = mPrefs.getString(KEY_PUBLIC_KEY, null)
        set(value) = mPrefs.edit().putString(KEY_PUBLIC_KEY, value).apply()

    override var otpExpiresIn
        get() = mPrefs.getString(KEY_OTP_EXPIRE_MIN, null)
        set(value) = mPrefs.edit().putString(KEY_OTP_EXPIRE_MIN, value).apply()


    override var fcmToken: String?
        get() = mPrefs.getString(FCM_TOKEN_KEY, null)
        set(value) = mPrefs.edit().putString(FCM_TOKEN_KEY, value).apply()

    override var balance: String?
        get() = mPrefs.getString(BALANCE_KEY, null)
        set(value) = mPrefs.edit().putString(BALANCE_KEY, value).apply()

    override var txRateLimits: MutableMap<String, Long>?
        get() {
            val jsonUser = mPrefs.getString(KEY_VALIDITY_RATE_MAP, null)
            return jsonUser?.let {
                Gson().fromJson(jsonUser, object : TypeToken<MutableMap<String, Long>?>() {}.type)
            }
        }
        set(value) {
            val jsonMap = Gson().toJson(value, object : TypeToken<MutableMap<String, Long>?>() {}.type)
            val editor = mPrefs.edit()
            editor.putString(KEY_VALIDITY_RATE_MAP, jsonMap)
            editor.apply()
        }

    override fun getString(key: String, defValue: String): String? = mPrefs.getString(key, defValue)

    override fun getInt(key: String, defValue: Int): Int? = mPrefs.getInt(key, defValue)

    override fun getBoolean(key: String, defValue: Boolean): Boolean = mPrefs.getBoolean(key, defValue)

    override fun putString(key: String, value: String) =
            mPrefs.edit().putString(key, value).apply()

    override fun putInt(key: String, value: Int) = mPrefs.edit().putInt(key, value).apply()

    override fun putBoolean(key: String, value: Boolean) = mPrefs.edit().putBoolean(key, value).apply()

    override fun logoutUser() {
        val editor = mPrefs.edit()

        editor.remove(PREF_KEY_ACCESS_TOKEN)
        editor.remove(PREF_KEY_TOKEN_TYPE)
        editor.remove(PREF_KEY_ACCESS_TOKEN_EXPIRES_IN)

        editor.remove(PREF_KEY_REFRESH_TOKEN)
        editor.remove(PREF_KEY_IS_LOGGED_IN)

        editor.remove(PREF_PERSONAL_INFO_KEY)
        editor.remove(KEY_USER_TYPE)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_EMAIL)

        editor.remove(KEY_MOBILE)
        editor.remove(BALANCE_KEY)
        editor.remove(TAP_TARGET_SHOW_KEY)

        editor.apply()
    }

    private fun getCurTime(): Long {
        return System.currentTimeMillis()
    }

    override val isAccessTokenExpired
        get() = getCurTime() > accessTokenExpiresIn


    override var lastActiveMillis: Long
        get() = mPrefs.getLong(PREF_KEY_LAST_ACTIVE_MILLIS, 0)
        set(value) = mPrefs.edit().putLong(PREF_KEY_LAST_ACTIVE_MILLIS, value).apply()

    override fun setLastActiveMillis() {
        lastActiveMillis = System.currentTimeMillis()
    }

    override var favList: List<String>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    companion object {

        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"

        private const val PREF_KEY_TOKEN_TYPE = "PREF_KEY_TOKEN_TYPE"

        private const val PREF_KEY_ACCESS_TOKEN_EXPIRES_IN = "PREF_KEY_ACCESS_TOKEN_EXPIRES_IN"

        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"

        private const val PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME"

        private const val PREF_KEY_IS_LOGGED_IN = "PREF_KEY_IS_LOGGED_IN"

        private const val PREF_PERSONAL_INFO_KEY = "PERSONAL_INFO_KEY"

        private const val KEY_USER_ID = "UserId"

        private const val KEY_EMAIL = "email"

        private const val KEY_MOBILE = "mobile"

        private const val KEY_VALIDITY_RATE_MAP = "KEY_VALIDITY_RATE"

        private const val KEY_COUNTRY_CODE = "CountryCode"

        private const val KEY_CURRENCY_SYMBOL = "CurrencySymbol"

        private const val KEY_OTP_EXPIRE_MIN = "OTPExpire"

        private const val KEY_CURRENCY_CODE = "LocalCurrency"

        private const val KEY_DATE_FORMAT = "dateFormat"

        private const val KEY_PUBLIC_KEY = "pub_key"

        private const val TAP_TARGET_SHOW_KEY = "tap_target_key"

        private const val FCM_TOKEN_KEY = "fcm_token"

        private const val BALANCE_KEY = "balance"

        private const val KEY_USER_TYPE = "USER_TYPE"
        private const val PREF_KEY_LAST_ACTIVE_MILLIS = "LAST_ACTIVE_MILLIS"
        private const val KEY_FAV_LIST = "KEY_FAV_LIST"

    }

}
