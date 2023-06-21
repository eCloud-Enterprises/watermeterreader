package com.ecloud.apps.watermeterreader.feature.reader.reader

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.ecloud.apps.watermeterreader.core.model.data.Consumption
import com.ecloud.apps.watermeterreader.core.model.data.getDisplayName
import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.feature.reader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MeterNo(
    list: List<Consumption>,
    onChangeSelectedOptionText: (Consumption, Int) -> Unit,
    modifier: Modifier = Modifier,
    state: TextFieldState = remember { TextFieldState() },
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: KeyboardActionScope.() -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = state.text,
            onValueChange = { state.text = it },
            label = { Text(text = stringResource(R.string.text_meter_no)) },
            modifier = modifier
                .menuAnchor()
                .onFocusChanged { focusState ->
                    state.onFocusChange(focusState.isFocused)
                },
            keyboardActions = KeyboardActions(
                onNext = onImeAction
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
                autoCorrect = false
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        val filteringOptions = list.filter {
            it.meterNo.contains(
                state.text,
                ignoreCase = true
            ) || it.location.contains(state.text, ignoreCase = true)
        }
        if (filteringOptions.isNotEmpty()) {
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = {}) {
                filteringOptions.forEachIndexed { index, selectedOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectedOption.getDisplayName()) },
                        onClick = {
                            onChangeSelectedOptionText(selectedOption, index)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }

}
