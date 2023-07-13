package com.ecloud.apps.watermeterreader.feature.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.WhileUiSubscribed
import com.ecloud.apps.watermeterreader.core.data.repository.ProjectRepository
import com.ecloud.apps.watermeterreader.core.model.data.ProjectWithConsumptions
import com.ecloud.apps.watermeterreader.core.result.Result
import com.ecloud.apps.watermeterreader.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(projectRepository: ProjectRepository) : ViewModel() {

    val uiState: StateFlow<ProjectsUiState> = projectRepository
        .getProjectsWithConsumptions()
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> ProjectsUiState.Error(
                    result.exception?.message ?: "Something went wrong"
                )

                Result.Loading -> ProjectsUiState.Loading
                is Result.Success -> ProjectsUiState.Success(result.data)
            }

        }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ProjectsUiState.Loading
        )
}

sealed interface ProjectsUiState {
    data class Success(
        val projectsWithConsumptions: List<ProjectWithConsumptions> = emptyList(),
    ) : ProjectsUiState

    object Loading : ProjectsUiState
    data class Error(val message: String) : ProjectsUiState
}


