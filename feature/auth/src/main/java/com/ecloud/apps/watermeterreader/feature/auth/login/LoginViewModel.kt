package com.ecloud.apps.watermeterreader.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.data.repository.AuthRepository
import com.ecloud.apps.watermeterreader.core.data.repository.AuthenticationFailed
import com.ecloud.apps.watermeterreader.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    userDataRepository: UserDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnLogin -> login(event)
            LoginEvent.OnSnackbarShown -> snackbarShown()
        }
    }

    private fun login(event: LoginEvent.OnLogin) {
        setLoading(true)

        viewModelScope.launch {
            try {
                authRepository.login(event.username.trim(), event.password.trim())
                showSnackbar("Authenticated")
            } catch (e: AuthenticationFailed) {
                showSnackbar(e.message)
            }
        }

        setLoading(false)
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(isLoading = loading) }
    }

    private fun snackbarShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun showSnackbar(message: String?) {
        _uiState.update { it.copy(userMessage = message) }
    }

}