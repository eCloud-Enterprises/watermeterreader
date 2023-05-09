package com.ecloud.apps.watermeterreader.feature.auth.network

import com.ecloud.apps.watermeterreader.core.model.data.NetworkUrl

sealed class NetworkSettingsEvent {
    data class OnSelectUrl(val networkUrl: NetworkUrl) : NetworkSettingsEvent()
    data class OnDeleteUrl(val networkUrl: NetworkUrl) : NetworkSettingsEvent()
}
