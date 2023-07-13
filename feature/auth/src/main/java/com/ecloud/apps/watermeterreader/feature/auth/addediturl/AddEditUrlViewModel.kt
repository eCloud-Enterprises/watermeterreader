package com.ecloud.apps.watermeterreader.feature.auth.addediturl

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.data.repository.UserDataRepository
import com.ecloud.apps.watermeterreader.core.result.Result
import com.ecloud.apps.watermeterreader.core.result.asResult
import com.ecloud.apps.watermeterreader.feature.auth.R
import com.ecloud.apps.watermeterreader.feature.auth.destinations.AddEditUrlScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Add/Edit Url Screen
 */
@HiltViewModel
class AddEditUrlViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = AddEditUrlScreenDestination.argsFrom(savedStateHandle)
    private val urlName = navArgs.name

    private val _uiState = MutableStateFlow(AddEditUrlUiState(topBarTitle = R.string.add_url))
    val uiState = _uiState.asStateFlow()

    init {
        if (urlName != null) {
            loadUrl(urlName)
        }
    }

    fun onEvent(event: AddEditUrlEvent) {
        when (event) {
            AddEditUrlEvent.OnSnackbarShown -> snackbarMessageShown()
            is AddEditUrlEvent.OnSubmit -> submit(event)
        }
    }

    private fun loadUrl(name: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            userDataRepository.userDataStream.map { it.customUrl }.asResult().map { result ->
                when (result) {
                    is Result.Error -> _uiState.update { it.copy(isLoading = false) }
                    Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        val customUrl = result.data.firstOrNull { it.name == name }
                            ?: throw RuntimeException("URL should exist")
                        val arr = customUrl.url.split("://")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                topBarTitle = R.string.edit_url,
                                protocol = arr.first(),
                                url = arr.last(),
                                name = customUrl.name
                            )
                        }
                    }
                }
            }
        }
    }

    private fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    private fun submit(event: AddEditUrlEvent.OnSubmit) {
        event.networkUrl.run {
            if (url.isEmpty() || name.isEmpty()) {
                _uiState.update {
                    it.copy(userMessage = "URL cannot be empty")
                }
                return
            }

            _uiState.update { it.copy(name = name, url = url) }

            if (urlName == null) {
                createNewUrl()
            } else {
                updateUrl()
            }
        }
    }

    private fun createNewUrl() = viewModelScope.launch {
        userDataRepository.addUrl(
            uiState.value.name,
            uiState.value.url
        )
        _uiState.update { it.copy(isSaved = true) }
    }

    private fun updateUrl() {
        if (urlName == null) {
            throw RuntimeException("updateUrl() was called but url is new.")
        }
        viewModelScope.launch {
            userDataRepository.updateUrlByName(
                uiState.value.name,
                uiState.value.url
            )
            if (navArgs.selected) {
                userDataRepository.setUrl(uiState.value.url)
            }
        }
        _uiState.update { it.copy(isSaved = true) }
    }
}

data class AddEditUrlUiState(
    val title: String = "",
    @StringRes val topBarTitle: Int = R.string.add_url,
    val userMessage: String? = null,
    val isLoading: Boolean = false,
    val protocol: String = "http",
    val url: String = "",
    val name: String = "",
    val isSaved: Boolean = false,
)
