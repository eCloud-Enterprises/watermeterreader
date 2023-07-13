package com.ecloud.apps.watermeterreader.feature.reader.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.ui.ProjectChips
import com.ecloud.apps.watermeterreader.core.ui.ProjectList
import com.ecloud.apps.watermeterreader.core.ui.rememberTextFieldState
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


                val textFieldState =
                    rememberTextFieldState(initialText = "")
                ProjectList(
                    list = uiState.projects,
                    state = textFieldState,
                    onChangeSelectedOptionText = { code, _ ->
                        onEvent(ProjectSelectEvent.OnSelectProject(code))
                    },

                    )

                if (uiState.selectedProjects.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    ProjectChips(
                        projects = uiState.selectedProjects,
                        onClick = { project -> onEvent(ProjectSelectEvent.OnDeselectProject(project)) },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEvent(ProjectSelectEvent.OnDownload) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Download")
                }
            }
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

