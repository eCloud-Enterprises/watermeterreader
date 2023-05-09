package com.ecloud.apps.watermeterreader.feature.onboarding.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.data.repository.NetworkRepository
import com.ecloud.apps.watermeterreader.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkScreenViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkScreenUiState())
    val state = _uiState.asStateFlow()

    fun onEvent(event: NetworkScreenEvent) {
        when (event) {
            is NetworkScreenEvent.OnApply -> apply(event)
            is NetworkScreenEvent.OnTest -> test(event)
        }
    }

    private fun apply(event: NetworkScreenEvent.OnApply) {
        viewModelScope.launch {
            with(userDataRepository) {
                addUrl(name = "default", url = event.url)
                setUrl(url = event.url)
            }
            _uiState.update { state ->
                state.copy(
                    shouldNavigate = true
                )
            }

        }

    }

    private fun test(event: NetworkScreenEvent.OnTest) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    testMessage = "Testing..."
                )
            }
            userDataRepository.setUrl(event.url)

            val isOk = try {
                networkRepository.testEndpoints()
            } catch (e: Exception) {
                userDataRepository.clearUrl()
                false
            }

            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    testMessage = if (isOk) "Success" else "Failed"
                )
            }

            delay(300)
            _uiState.update { state ->
                state.copy(testMessage = "")
            }
        }

    }
}