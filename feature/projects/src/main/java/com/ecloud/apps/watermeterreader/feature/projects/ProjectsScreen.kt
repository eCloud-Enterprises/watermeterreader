package com.ecloud.apps.watermeterreader.feature.projects

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecloud.apps.watermeterreader.core.designsystem.components.EbtLoader
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.model.data.WaterReadingItem
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.core.model.data.ProjectWithConsumptions
import com.ecloud.apps.watermeterreader.core.model.data.getDisplayName
import com.ramcosta.composedestinations.annotation.Destination

val mockProjectWithConsumptions = List(10) {
    ProjectWithConsumptions(
        project = Project(it.toString().padStart(4, '0'), name = "Project ${it + 1}"),
        waterReadingItems = List(3) { num ->
            WaterReadingItem(
                meterNo = num.toString().padStart(4, '0'),
                currentReading = 0.0f,
                consumption = 0f,
                previousReading = 0f,
                remarks = "",
                adjustments = 0f,
                projectCode = "",
                location = "Makati-BL${num + 10}-L${num + 9}",
                contract = ""
            )
        }
    )
}


@Destination
@Composable
fun ProjectsScreen() {
    val viewModel = hiltViewModel<ProjectsViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    ProjectsScreen(uiState = uiState)
}

@Composable
fun ProjectsScreen(
    uiState: ProjectsUiState,
) {
    when (uiState) {
        is ProjectsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error")
            }
        }
        ProjectsUiState.Loading -> ProjectsLoading()
        is ProjectsUiState.Success -> {
            ProjectItems(
                projects = uiState.projectsWithConsumptions,
            )
        }
    }
}

@Composable
fun ProjectsLoading(modifier: Modifier = Modifier) {
    EbtLoader(modifier.fillMaxSize())
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ProjectItems(projects: List<ProjectWithConsumptions>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(projects, key = { it.project.code }) { item ->
            ProjectItem(
                item = item,
                modifier = Modifier
                    .animateItemPlacement(),
                onDelete = {}
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProjectItem(
    item: ProjectWithConsumptions,
    modifier: Modifier = Modifier,
    onDelete: (Project) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Project ${item.project.name}") },
            text = { Text(text = "Are you sure you want to delete this project from your downloaded projects?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(item.project)
                    expanded = false
                }) {
                    Text(text = stringResource(R.string.text_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { expanded = false }) {
                    Text(text = stringResource(R.string.text_cancel))
                }
            })
    }
    Column(modifier = modifier) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.project.code)
                Text(text = item.project.name, style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = { expanded = !expanded }) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.text_delete)
                    )
                }
            }
        }
        AnimatedVisibility(visible = expanded) {

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item.waterReadingItems.forEach { consumption ->
                    Text(text = consumption.getDisplayName())
                }
            }
        }

    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProjectScreenPreview() {
    WmrTheme {
        ProjectsScreen(uiState = ProjectsUiState.Success(projectsWithConsumptions = mockProjectWithConsumptions))
    }
}
