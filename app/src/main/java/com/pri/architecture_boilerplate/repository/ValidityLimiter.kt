package com.pri.architecture_boilerplate.repository

import com.pri.architecture_boilerplate.prefs.PreferencesHelper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class that decides whether we should fetch some customerMenu or not.
 */
@Singleton
class ValidityLimiter @Inject constructor(val preferencesHelper: PreferencesHelper) {

    private val timestamps: MutableMap<String, Long> = preferencesHelper.txRateLimits ?: HashMap()
    private val timeout: Long = 30 * 60 * 1000 /* default timeout 30 minutes */

    @Synchronized
    fun shouldFetch(key: String): Boolean {
        val lastFetched = timestamps.get(key)
        if (lastFetched == null || now() - lastFetched > timeout) {
            setLastFetchTime(key)
            return true
        }
        return false
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }

    @Synchronized
    fun reset(key: String) {
        timestamps.remove(key)
        preferencesHelper.txRateLimits = timestamps
    }

    @Synchronized
    fun setLastFetchTime(key: String) {
        timestamps[key] = now()
        preferencesHelper.txRateLimits = timestamps
    }

    companion object {
        const val CONST_CAHE_VALIDITY = "CONST_CAHE_VALIDITY"
        const val PROFIlE_INFO_CAHE_VALIDITY = "PROFIlE_INFO_CAHE_VALIDITY"
    }

}