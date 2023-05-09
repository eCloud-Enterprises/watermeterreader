package com.ecloud.apps.watermeterreader.feature.auth.network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecloud.apps.watermeterreader.core.designsystem.components.EbtTopAppBar
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.feature.auth.AuthNavigator
import com.ecloud.apps.watermeterreader.feature.auth.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun NetworkSettingsScreen(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<NetworkSettingsViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    NetworkSettingsScreen(state = state, onEvent = viewModel::onEvent, onNavigationEvent = { event ->
        when (event) {
            NetworkSettingsNavigationEvent.OnNavigateBack -> navigator.navigateUp()
            is NetworkSettingsNavigationEvent.OnNavigateToForm -> {}
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NetworkSettingsScreen(
    state: NetworkSettingsUiState,
    modifier: Modifier = Modifier,
    onNavigationEvent: (NetworkSettingsNavigationEvent) -> Unit = {},
    onEvent: (NetworkSettingsEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.add_url)) },
                icon = { Icon(imageVector = EbtIcons.Add, contentDescription = null) },
                onClick = {
                    onNavigationEvent(
                        NetworkSettingsNavigationEvent.OnNavigateToForm(
                            null,
                            false
                        )
                    )
                },
            )
        },
        topBar = {
            EbtTopAppBar(
                onNavigationClick = { onNavigationEvent(NetworkSettingsNavigationEvent.OnNavigateBack) },
                titleRes = R.string.network_settings,
                navigationIcon = EbtIcons.ArrowBack,
                navigationIconContentDescription = stringResource(id = R.string.navigate_back)
            )
        },
    ) { padding ->
        when (state) {
            is NetworkSettingsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.urls, key = { it.name }) { url ->
                        UrlItem(
                            selected = state.selectedUrl == url,
                            url = url,
                            onSelectUrl = { onEvent(NetworkSettingsEvent.OnSelectUrl(url)) },
                            onNavigateToAddEdit = { name, selected ->
                                onNavigationEvent(
                                    NetworkSettingsNavigationEvent.OnNavigateToForm(name, selected)
                                )
                            },
                            onDeleteUrlClick = { onEvent(NetworkSettingsEvent.OnDeleteUrl(url)) }
                        )
                    }
                }
            }

            NetworkSettingsUiState.Error -> {}
            NetworkSettingsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NetworkSettingsPreview() {
    WmrTheme {
        NetworkSettingsScreen(state = NetworkSettingsUiState.Success(), onEvent = {})
    }
}

@Preview
@Composable
fun NetworkSettingsPreviewDark() {
    WmrTheme(darkTheme = true) {
        NetworkSettingsScreen(state = NetworkSettingsUiState.Success(), onEvent = {})
    }
}
