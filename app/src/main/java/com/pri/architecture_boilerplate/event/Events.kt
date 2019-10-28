package com.pri.architecture_boilerplate.event

/**
 * Created by Priyanka on 11/3/19.
 */
class ErrorMessageEvent(val message: String, val showInAlert:Boolean)
class LoadingEvent(val loading: Boolean)
class ForegroundEvent
class RefreshTokenExpireEvent
class LogoutEvent(val showAlert:Boolean = false)