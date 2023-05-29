package com.ecloud.apps.watermeterreader.feature.reader.reader

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.data.repository.ConsumptionRepository
import com.ecloud.apps.watermeterreader.core.data.repository.ProjectRepository
import com.ecloud.apps.watermeterreader.core.model.data.Consumption
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.core.model.data.ProjectWithConsumptions
import com.ecloud.apps.watermeterreader.core.result.Result
import com.ecloud.apps.watermeterreader.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val consumptionRepository: ConsumptionRepository,
    projectRepository: ProjectRepository
) :
    ViewModel() {

    private val projectsStream = projectRepository.getProjectsWithConsumptions().asResult()

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            projectsStream.collect { result ->
                when (result) {
                    is Result.Error -> TODO()
                    Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        Log.d("ReaderViewModel", result.data.map { it.code }.toString())
                        val list = result.data.toMutableList()
                        list.add(0, ProjectWithConsumptions("Select here", "", emptyList()))
                        _uiState.update { it.copy(projects = list, isLoading = false) }
                    }
                }
            }
        }

    }

    fun onEvent(event: ReaderEvent) {
        when (event) {
            is ReaderEvent.OnScan -> scan(event)
            is ReaderEvent.OnSave -> save(event)
            is ReaderEvent.OnSelectProject -> selectProject(event)
        }
    }

    private fun selectProject(event: ReaderEvent.OnSelectProject) {
        val project = _uiState.value.projects.first { it.code == event.projectCode }
        _uiState.update { it.copy(selectedProject = project) }
    }

    private fun save(event: ReaderEvent.OnSave) {

        _uiState.value.run {
            checkNotNull(selectedConsumption) { "Consumption must be set beforehand" }
            val consumption = selectedConsumption.copy(
                currentReading = event.currentReading.toFloat(),
                adjustments = event.adjustments.toFloat(),
                remarks = event.remarks
            )

            consumptionRepository.updateConsumption(consumption)

            // save to database
        }
    }

    private fun scan(event: ReaderEvent.OnScan) {
        val project =
            checkNotNull(_uiState.value.selectedProject) { "Project must be set beforehand" }

        val consumption =
            project
                .consumptions.first { consumption -> consumption.meterNo == event.meterNo }
        _uiState.update { it.copy(selectedConsumption = consumption) }
    }
}

sealed class ReaderEvent {
    data class OnScan(val meterNo: String) : ReaderEvent()
    data class OnSave(
        val currentReading: Int,
        val adjustments: Int,
        val remarks: String
    ) : ReaderEvent()

    data class OnSelectProject(val projectCode: String) : ReaderEvent()
}

data class ReaderUiState(
    val projects: List<ProjectWithConsumptions> = emptyList(),
    val selectedProject: ProjectWithConsumptions? = null,
    val selectedConsumption: Consumption? = null,
    val isLoading: Boolean = true
)