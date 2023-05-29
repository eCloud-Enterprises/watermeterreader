package com.ecloud.apps.watermeterreader.feature.reader.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.feature.reader.ProjectList
import com.ecloud.apps.watermeterreader.feature.reader.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProjectSelectScreen(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<ProjectSelectViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProjectSelectScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onDownload = navigator::navigateUp
    )

}

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun ProjectSelectScreen(
    uiState: ProjectSelectUiState,
    modifier: Modifier = Modifier,
    onEvent: (ProjectSelectEvent) -> Unit = {},
    onDownload: () -> Unit = {}
) {
    if (uiState.shouldNavigate) {
        LaunchedEffect(key1 = Unit, block = {
            onDownload()
        })
    }
    Surface(modifier = modifier) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.text_projects),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.select_a_project),
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(24.dp))


                var selectedIndex by remember { mutableStateOf(0) }
                var selectedText by remember { mutableStateOf(uiState.projects[selectedIndex].name) }
                ProjectList(
                    list = uiState.projects.map { it.code },
                    selectedOptionText = selectedText,
                    onChangeSelectedOptionText = { code, index ->
                        selectedIndex = index
                        onEvent(ProjectSelectEvent.OnSelectProject(code))
                    },
                    onSelectedOptionTextChange = { selectedText = it }
                )

                if (uiState.selectedProjects.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))



                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .wrapContentHeight(align = Alignment.Top),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        uiState.selectedProjects.forEach { project ->
                            ProjectChip(
                                project = project,
                                onClick = onEvent,
                                modifier = Modifier.align(alignment = CenterVertically)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEvent(ProjectSelectEvent.OnDownload) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.text_download))
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectChip(
    project: Project,
    onClick: (ProjectSelectEvent.OnDeselectProject) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        var openDialog by remember { mutableStateOf(false) }
        AssistChip(
            label = { Text(text = project.name) },
            onClick = {
                openDialog = true
            },
            trailingIcon = {
                Icon(
                    imageVector = EbtIcons.FilledClose,
                    contentDescription = "Deselect Project",
                    modifier = Modifier.size(AssistChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = LocalContentAlpha.current)
                )
            },
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                title = {
                    Text(text = "Deselect Project ${project.name}")
                },
                icon = {
                    Icon(
                        imageVector = EbtIcons.FilledClose,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = LocalContentAlpha.current)
                    )
                },
                text = { Text(text = "Are you sure you want to deselect this project for download?") },
                confirmButton = {
                    TextButton(onClick = {

                        onClick(ProjectSelectEvent.OnDeselectProject(project.code))
                        openDialog = false
                    }) {
                        Text(text = "Deselect")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {

                        openDialog = false
                    }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun ProjectSelectScreenPreview() {
    WmrTheme {
        ProjectSelectScreen(uiState = ProjectSelectUiState())
    }
}

