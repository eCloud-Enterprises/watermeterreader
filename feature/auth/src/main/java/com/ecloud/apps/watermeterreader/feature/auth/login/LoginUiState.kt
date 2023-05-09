package com.ecloud.apps.watermeterreader.feature.auth.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val toWelcomeScreen: Boolean = false,
)
