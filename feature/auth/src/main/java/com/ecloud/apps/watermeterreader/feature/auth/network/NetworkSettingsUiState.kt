package com.ecloud.apps.watermeterreader.feature.auth.network

import com.ecloud.apps.watermeterreader.core.model.data.NetworkUrl

sealed class NetworkSettingsUiState {
    data class Success(
        val urls: List<NetworkUrl> = emptyList(),
        val selectedUrl: NetworkUrl? = null
    ) : NetworkSettingsUiState()

    object Error : NetworkSettingsUiState()

    object Loading : NetworkSettingsUiState()
}
