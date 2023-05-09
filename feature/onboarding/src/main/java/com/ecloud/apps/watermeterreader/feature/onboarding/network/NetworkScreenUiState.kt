package com.ecloud.apps.watermeterreader.feature.onboarding.network

data class NetworkScreenUiState(
    val isLoading: Boolean = false,
    val shouldNavigate: Boolean = false,
    val testMessage: String = ""
)
