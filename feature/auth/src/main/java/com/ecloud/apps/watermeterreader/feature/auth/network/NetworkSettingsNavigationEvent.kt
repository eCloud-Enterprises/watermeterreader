package com.ecloud.apps.watermeterreader.feature.auth.network

sealed class NetworkSettingsNavigationEvent {
    object OnNavigateBack: NetworkSettingsNavigationEvent()
    data class OnNavigateToForm(val name: String?, val selected: Boolean): NetworkSettingsNavigationEvent()
}
