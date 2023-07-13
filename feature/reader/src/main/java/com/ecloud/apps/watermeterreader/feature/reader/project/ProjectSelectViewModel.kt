package com.ecloud.apps.watermeterreader.feature.reader.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.data.repository.ConsumptionRepository
import com.ecloud.apps.watermeterreader.core.data.repository.ProjectRepository
import com.ecloud.apps.watermeterreader.core.model.data.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectSelectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val consumptionRepository: ConsumptionRepository
) : ViewModel() {

    private val projectStream = projectRepository.getProjectsStream()

    private val _uiState = MutableStateFlow(ProjectSelectUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            projectRepository.fetchProjects()
            val result = projectStream.first()
            _uiState.update { it.copy(projects = result, isLoading = false) }
        }
    }

    fun onEvent(event: ProjectSelectEvent) {
        when (event) {
            ProjectSelectEvent.OnDownload -> download()
            is ProjectSelectEvent.OnSelectProject -> selectProject(event)
            is ProjectSelectEvent.OnDeselectProject -> deselectProject(event)
        }
    }

    private fun deselectProject(event: ProjectSelectEvent.OnDeselectProject) {
        val selectedProjects = _uiState.value.selectedProjects.toMutableList()
        selectedProjects.remove(event.project)
        _uiState.update { it.copy(selectedProjects = selectedProjects) }
    }

    private fun selectProject(event: ProjectSelectEvent.OnSelectProject) {
        val selectedProjects = _uiState.value.selectedProjects + event.project
        _uiState.update { it.copy(selectedProjects = selectedProjects) }
    }

    private fun download() {
        viewModelScope.launch {
            consumptionRepository.fetchConsumptions(_uiState.value.selectedProjects.map { it.code })
            _uiState.update { it.copy(shouldNavigate = true) }
        }
    }

}

sealed class ProjectSelectEvent {
    data class OnSelectProject(val project: Project) : ProjectSelectEvent()
    object OnDownload : ProjectSelectEvent()
    data class OnDeselectProject(val project: Project) : ProjectSelectEvent()
}

data class ProjectSelectUiState(
    val projects: List<Project> = emptyList(),
    val selectedProjects: List<Project> = emptyList(),
    val shouldNavigate: Boolean = false,
    val isLoading: Boolean = true
)