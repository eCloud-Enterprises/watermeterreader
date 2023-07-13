package com.ecloud.apps.watermeterreader.feature.reader.reader

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.model.data.getDisplayName
import com.ecloud.apps.watermeterreader.core.ui.ProjectList
import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.feature.reader.R
import com.ecloud.apps.watermeterreader.feature.reader.destinations.ProjectSelectScreenDestination
import com.ecloud.apps.watermeterreader.feature.reader.reader.states.AdjustmentState
import com.ecloud.apps.watermeterreader.feature.reader.reader.states.AdjustmentStateSaver
import com.ecloud.apps.watermeterreader.feature.reader.reader.states.MeterNoState
import com.ecloud.apps.watermeterreader.feature.reader.reader.states.MeterNoStateSaver
import com.ecloud.apps.watermeterreader.feature.reader.reader.states.ReadingsState
import com.ecloud.apps.watermeterreader.feature.reader.reader.states.RemarksState
import com.ecloud.apps.watermeterreader.feature.reader.reader.states.RemarksStateSaver
import com.google.android.gms.common.moduleinstall.ModuleInstallClient
import com.google.android.gms.tflite.client.TfLiteClient
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Destination
@Composable
fun ReaderScreen(navigator: DestinationsNavigator, snackbarHostState: SnackbarHostState) {
    val viewModel = hiltViewModel<ReaderViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE
        )
        .build()

    val scanner = GmsBarcodeScanning.getClient(LocalContext.current, options)
    ReaderScreen(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        scanner = scanner,
        onEvent = viewModel::onEvent,
        showSelectionPage = { navigator.navigate(ProjectSelectScreenDestination) }
    )

}


@Composable
internal fun ReaderScreen(
    uiState: ReaderUiState,
    modifier: Modifier = Modifier,
    scanner: GmsBarcodeScanner,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEvent: (ReaderEvent) -> Unit = {},
    showSelectionPage: () -> Unit = {}
) {
    if (!uiState.isLoading && uiState.shouldDownloadProjects) {
        LaunchedEffect(key1 = Unit, block = { showSelectionPage() })
    }

    if (uiState.scanError.isNotEmpty()) {
        var open by remember { mutableStateOf(false) }
        if (open) {
            AlertDialog(
                onDismissRequest = { open = false },
                title = { Text("Scan Error") },
                text = {
                    Text(
                        text = uiState.scanError
                    )
                },
                confirmButton = {
                    TextButton(onClick = { open = false }) {
                        Text(text = "Understood")
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
                }
            )
        }
    }

    val scope = rememberCoroutineScope()
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val adjustmentFr = remember { FocusRequester() }
            val remarksFr = remember { FocusRequester() }
            val meterNoFr = remember { FocusRequester() }
            val readingFr = remember { FocusRequester() }

            val projectState =
                remember { TextFieldState() }
            ProjectList(
                list = uiState.projectsWithConsumptions.map { it.project },
                state = projectState,
                onChangeSelectedOptionText = { project, _ ->
                    onEvent(ReaderEvent.OnSelectProject(project))
                    projectState.text = project.getDisplayName()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            uiState.selectedProject?.let { project ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val meterNoState by rememberSaveable(stateSaver = MeterNoStateSaver) {
                        mutableStateOf(
                            MeterNoState()
                        )
                    }

                    MeterNo(
                        list = project.waterReadingItems,
                        state = meterNoState,
                        onImeAction = { readingFr.requestFocus() },
                        imeAction = ImeAction.Next,
                        modifier = Modifier
                            .focusRequester(meterNoFr)
                            .weight(1f),
                        onChangeSelectedOptionText = { consumption, _ ->
                            onEvent(ReaderEvent.OnSelectConsumption(consumption))
                            meterNoState.text = consumption.getDisplayName()
                            readingFr.requestFocus()
                        }
                    )


                    ScanButton(onClick = {
                        scope.launch {
                            try {
                                val barcode = scanner.open()
                                barcode?.let {
                                    onEvent(ReaderEvent.OnScan(barcode))
                                    readingFr.requestFocus()
                                    Log.d("QRCode Scan", barcode)
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar(message = "Scan Failed, something went wrong")
                                Log.e("QRCode Error", e.message ?: "Something went wrong")
                            }
                        }
                    })
                }
                Spacer(modifier = Modifier.height(16.dp))

                val readingState = remember { ReadingsState() }

                CurrentReading(
                    state = readingState,
                    onImeAction = { adjustmentFr.requestFocus() },
                    imeAction = ImeAction.Next,
                    modifier = Modifier.focusRequester(readingFr)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val adjustmentState by rememberSaveable(stateSaver = AdjustmentStateSaver) {
                    mutableStateOf(AdjustmentState())
                }

                Adjustments(
                    state = adjustmentState,
                    modifier = Modifier.focusRequester(adjustmentFr),
                    onImeAction = {
                        if (adjustmentState.text.isNotEmpty()) {
                            remarksFr.requestFocus()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))

                val remarksState by rememberSaveable(stateSaver = RemarksStateSaver) {
                    mutableStateOf(RemarksState())
                }
                if (adjustmentState.text.isNotEmpty()) {

                    Remarks(
                        state = remarksState,
                        modifier = Modifier.focusRequester(remarksFr),
                        imeAction = ImeAction.Done,
                        onImeAction = {
                            onEvent(
                                ReaderEvent.OnSave(
                                    currentReading = readingState.text.toInt(),
                                    adjustments = adjustmentState.text.toInt(),
                                    remarks = remarksState.text
                                )
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Button(onClick = {

                    onEvent(
                        ReaderEvent.OnSave(
                            currentReading = readingState.text.toInt(),
                            adjustments = adjustmentState.text.toInt(),
                            remarks = remarksState.text
                        )
                    )
                }) {
                    Text(text = "Update")
                }
            }
        }
    }
}

@Composable
internal fun ScanButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
    ) {
        Text(text = "Scan QR Code")
    }
}

@OptIn(InternalCoroutinesApi::class)
suspend fun GmsBarcodeScanner.open(): String? = suspendCancellableCoroutine { cont ->
    startScan()
        .addOnCanceledListener {
            cont.cancel()
        }
        .addOnFailureListener { e ->
            cont.resumeWithException(e)
        }
        .addOnSuccessListener { barcode ->
            cont.tryResume(barcode.rawValue)
        }

}

suspend fun ModuleInstallClient.getAvailability(module: TfLiteClient): Boolean =
    suspendCancellableCoroutine { cont ->
        areModulesAvailable(module)
            .addOnSuccessListener { result ->
                cont.resume(result.areModulesAvailable())
            }
            .addOnCanceledListener { cont.cancel() }
            .addOnFailureListener { cont.cancel(it) }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReaderToolbar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.text_reader)) },
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ReaderScreenPreview() {
    WmrTheme {
        ReaderScreen(
            uiState = ReaderUiState(),
            scanner = GmsBarcodeScanning.getClient(LocalContext.current)
        )
    }
}

