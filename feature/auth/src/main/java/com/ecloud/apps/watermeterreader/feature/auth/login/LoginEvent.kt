package com.ecloud.apps.watermeterreader.feature.auth.login

sealed class LoginEvent {
    object OnSnackbarShown : LoginEvent()

    data class OnLogin(val username: String, val password: String) : LoginEvent()
}
