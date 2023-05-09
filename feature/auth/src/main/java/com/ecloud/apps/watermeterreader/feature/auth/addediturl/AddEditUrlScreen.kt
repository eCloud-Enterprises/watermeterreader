package com.ecloud.apps.watermeterreader.feature.auth.addediturl

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecloud.apps.watermeterreader.core.designsystem.components.EbtTopAppBar
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.model.data.NetworkUrl
import com.ecloud.apps.watermeterreader.core.ui.ProtocolDropdown
import com.ecloud.apps.watermeterreader.core.ui.UrlNameState
import com.ecloud.apps.watermeterreader.core.ui.UrlNameStateSaver
import com.ecloud.apps.watermeterreader.core.ui.UrlState
import com.ecloud.apps.watermeterreader.core.ui.UrlStateSaver
import com.ecloud.apps.watermeterreader.core.ui.UrlTextFieldUserInput
import com.ecloud.apps.watermeterreader.feature.auth.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

data class AddEditUrlRouteNavArgs(
    val name: String?,
    val selected: Boolean = false
)

object FullScreenDialog : DestinationStyle.Dialog {
    override val properties: DialogProperties
        get() = DialogProperties(
            dismissOnClickOutside = false, usePlatformDefaultWidth = false
        )
}

@Destination(
    navArgsDelegate = AddEditUrlRouteNavArgs::class,
    style = FullScreenDialog::class
)
@Composable
fun AddEditUrlScreen(
    navigator: DestinationsNavigator,
    snackbarHostState: SnackbarHostState
) {

    val viewModel = hiltViewModel<AddEditUrlViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AddEditUrlScreen(
        uiState = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        onNavigateUp = navigator::navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddEditUrlScreen(
    uiState: AddEditUrlUiState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEvent: (AddEditUrlEvent) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    uiState.userMessage?.let { message ->
        LaunchedEffect(key1 = uiState, message, block = {
            snackbarHostState.showSnackbar(message)
            onEvent(AddEditUrlEvent.OnSnackbarShown)
        })
    }
    val urlState by rememberSaveable(stateSaver = UrlStateSaver) {
        mutableStateOf(UrlState(uiState.url))
    }
    val urlNameState by rememberSaveable(stateSaver = UrlNameStateSaver) {
        mutableStateOf(UrlNameState(uiState.name))
    }
    val items = listOf("http", "https")
    var selectedIndex by remember { mutableStateOf(items.indexOf(uiState.protocol)) }

    val onSubmit = {
        onEvent(
            AddEditUrlEvent.OnSubmit(
                NetworkUrl(
                    urlNameState.text,
                    items[selectedIndex] + "://" + urlState.text
                )
            )
        )
    }

    if (uiState.isSaved) {
        LaunchedEffect(true) {
            onNavigateUp()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                titleRes = uiState.topBarTitle,
                onNavigateUp = onNavigateUp,
                onSubmitClick = onSubmit,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            val urlFocusRequester = remember { FocusRequester() }

            Name(
                nameState = urlNameState,
                enabled = uiState.topBarTitle == R.string.add_url,
                onImeAction = { urlFocusRequester.requestFocus() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProtocolDropdown(
                onItemClick = { selectedIndex = it },
                selectedIndex = selectedIndex,
                items = items,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            UrlTextFieldUserInput(
                urlTextFieldState = urlState,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(urlFocusRequester),
                onImeAction = onSubmit
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    @StringRes titleRes: Int,
    onNavigateUp: () -> Unit,
    onSubmitClick: () -> Unit,
    enabled: Boolean = true,
) {
    EbtTopAppBar(
        onNavigationClick = onNavigateUp, titleRes = titleRes,
        actionIcon = EbtIcons.Check,
        actionIconContentDescription = stringResource(id = R.string.submit_form),
        onActionClick = {
            if (enabled) {
                onSubmitClick()
            }
        },
        navigationIcon = EbtIcons.ArrowBack,
        navigationIconContentDescription = stringResource(id = R.string.navigate_back)
    )
}

@Preview
@Composable
private fun AddEditUrlScreenPreview() {
    WmrTheme {
        AddEditUrlScreen(uiState = AddEditUrlUiState())
    }
}

@Preview
@Composable
private fun AddEditUrlScreenPreviewDark() {
    WmrTheme(darkTheme = true) {
        AddEditUrlScreen(uiState = AddEditUrlUiState())
    }
}
