package com.ecloud.apps.watermeterreader.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.core.model.data.getDisplayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectList(
    list: List<Project>,
    state: TextFieldState = remember { TextFieldState() },
    onChangeSelectedOptionText: (project: Project, index: Int) -> Unit,
    imeAction: ImeAction = ImeAction.Done,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = state.text,
            onValueChange = { state.text = it },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.project_list)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            )
        )
        val filteringOptions = list.filter {
            it.name.contains(
                state.text,
                ignoreCase = true
            ) || it.code.contains(state.text, ignoreCase = true)
        }
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = {}) {
            filteringOptions.forEachIndexed { index, selectOption ->
                DropdownMenuItem(
                    text = { Text(text = selectOption.getDisplayName()) },
                    onClick = {
                        onChangeSelectedOptionText(selectOption, index)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }

    }
}
