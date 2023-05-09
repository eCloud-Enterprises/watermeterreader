package com.ecloud.apps.watermeterreader.feature.onboarding.network

sealed class NetworkScreenEvent {
    data class OnApply(val url: String) : NetworkScreenEvent()
    data class OnTest(val url: String): NetworkScreenEvent()
}
