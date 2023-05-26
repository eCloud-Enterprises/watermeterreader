package com.ecloud.apps.watermeterreader.feature.reader

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.feature.reader.states.AdjustmentState
import com.ecloud.apps.watermeterreader.feature.reader.states.AdjustmentStateSaver
import com.ecloud.apps.watermeterreader.feature.reader.states.ReadingState
import com.ecloud.apps.watermeterreader.feature.reader.states.ReadingStateSaver
import com.ecloud.apps.watermeterreader.feature.reader.states.RemarksState
import com.ecloud.apps.watermeterreader.feature.reader.states.RemarksStateSaver
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ReaderScreen(navigator: DestinationsNavigator, snackbarHostState: SnackbarHostState) {
    ReaderScreen(snackbarHostState = snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReaderScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Offline",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.inverseSurface)
                    .padding(8.dp),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    textAlign = TextAlign.Center
                )
            )
            ReaderToolbar()

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {


                val adjustmentFr = remember { FocusRequester() }
                val remarksFr = remember { FocusRequester() }
                val readingFr = remember { FocusRequester() }

                DraftList(
                    auditList = listOf("Hello", "Hi"),
                    selectedOptionText = "",
                    onChangeSelectedOptionText = {
                        adjustmentFr.requestFocus()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))


                CurrentReading()

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
                        } else {
                            readingFr.requestFocus()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (adjustmentState.text.isNotEmpty()) {
                    val remarksState by rememberSaveable(stateSaver = RemarksStateSaver) {
                        mutableStateOf(RemarksState())
                    }

                    Remarks(
                        state = remarksState,
                        modifier = Modifier.focusRequester(remarksFr),
                        imeAction = ImeAction.Next,
                        onImeAction = { readingFr.requestFocus() },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }


                val readingState by rememberSaveable(stateSaver = ReadingStateSaver) {
                    mutableStateOf(
                        ReadingState()
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NewReading(
                        state = readingState,
                        onImeAction = {},
                        modifier = Modifier
                            .focusRequester(readingFr)
                            .weight(1f)
                    )
                    IconButton(
                        onClick = { /*TODO*/ }, colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = EbtIcons.QrCodeScanner),
                            contentDescription = "Scanner"
                        )
                    }
                }


            }
        }
    }
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
        ReaderScreen()
    }
}

