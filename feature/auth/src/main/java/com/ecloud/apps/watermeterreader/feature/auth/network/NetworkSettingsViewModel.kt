package com.ecloud.apps.watermeterreader.feature.auth.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.WhileUiSubscribed
import com.ecloud.apps.watermeterreader.core.data.repository.UserDataRepository
import com.ecloud.apps.watermeterreader.core.result.Result
import com.ecloud.apps.watermeterreader.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkSettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val uiState: StateFlow<NetworkSettingsUiState> =
        networkSettingsUiState(userDataRepository)
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = NetworkSettingsUiState.Loading
            )


    fun onEvent(event: NetworkSettingsEvent) {
        when (event) {
            is NetworkSettingsEvent.OnDeleteUrl -> delete(event)
            is NetworkSettingsEvent.OnSelectUrl -> select(event)
        }
    }

    private fun select(event: NetworkSettingsEvent.OnSelectUrl) {
        viewModelScope.launch { userDataRepository.setUrl(event.networkUrl.url) }
    }

    private fun delete(event: NetworkSettingsEvent.OnDeleteUrl) {
        viewModelScope.launch { userDataRepository.deleteUrlByKey(event.networkUrl.name) }
    }
}

private fun networkSettingsUiState(
    userDataRepository: UserDataRepository
): Flow<NetworkSettingsUiState> {
    val userData = userDataRepository.userDataStream

    return userData.asResult().map { result ->
        when (result) {
            is Result.Error -> NetworkSettingsUiState.Error
            Result.Loading -> NetworkSettingsUiState.Loading
            is Result.Success -> {
                val user = result.data
                NetworkSettingsUiState.Success(
                    urls = user.customUrl,
                    selectedUrl = user.customUrl.find { it.url == user.selectedUrl }
                )
            }
        }
    }
}