package com.ecloud.apps.watermeterreader.feature.reader.reader

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecloud.apps.watermeterreader.core.data.repository.ConsumptionRepository
import com.ecloud.apps.watermeterreader.core.data.repository.ProjectRepository
import com.ecloud.apps.watermeterreader.core.model.data.WaterReadingItem
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.core.model.data.ProjectWithConsumptions
import com.ecloud.apps.watermeterreader.core.result.Result
import com.ecloud.apps.watermeterreader.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                    is Result.Error -> {
                        Log.e(
                            "ReaderViewModel",
                            result.exception?.message ?: "Something went wrong"
                        )
                    }

                    Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                projectsWithConsumptions = result.data,
                                isLoading = false,
                                shouldDownloadProjects = result.data.isEmpty()
                            )
                        }
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
            is ReaderEvent.OnSelectConsumption -> selectConsumption(event)
            ReaderEvent.OnScanErrorShown -> _uiState.update { it.copy(scanError = "") }
        }
    }

    private fun selectConsumption(event: ReaderEvent.OnSelectConsumption) {
        _uiState.update { it.copy(selectedWaterReadingItem = event.waterReadingItem) }
    }

    private fun selectProject(event: ReaderEvent.OnSelectProject) {
        val project =
            _uiState.value.projectsWithConsumptions.first { it.project.code == event.project.code }
        _uiState.update { it.copy(selectedProject = project) }
    }

    private fun save(event: ReaderEvent.OnSave) {

        _uiState.value.run {
            checkNotNull(selectedWaterReadingItem) { "Consumption must be set beforehand" }
            val consumption = selectedWaterReadingItem.copy(
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
                .waterReadingItems.firstOrNull { consumption -> consumption.meterNo == event.meterNo }

        if (consumption == null) {
            _uiState.update { it.copy(scanError = "Meter No is not found") }
        } else {
            _uiState.update { it.copy(selectedWaterReadingItem = consumption) }
        }
    }
}

sealed class ReaderEvent {
    data class OnScan(val meterNo: String) : ReaderEvent()
    data class OnSave(
        val currentReading: Int,
        val adjustments: Int,
        val remarks: String
    ) : ReaderEvent()

    data class OnSelectProject(val project: Project) : ReaderEvent()
    data class OnSelectConsumption(val waterReadingItem: WaterReadingItem) : ReaderEvent()
    object OnScanErrorShown : ReaderEvent()
}

data class ReaderUiState(
    val projectsWithConsumptions: List<ProjectWithConsumptions> = emptyList(),
    val selectedProject: ProjectWithConsumptions? = null,
    val selectedWaterReadingItem: WaterReadingItem? = null,
    val isLoading: Boolean = true,
    val scanError: String = "",
    val shouldDownloadProjects: Boolean =false
)