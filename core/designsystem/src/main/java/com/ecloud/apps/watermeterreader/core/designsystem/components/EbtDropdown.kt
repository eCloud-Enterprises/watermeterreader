package com.ecloud.apps.watermeterreader.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.ecloud.apps.watermeterreader.core.designsystem.icon.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EbtDropdown(
    items: List<String>,
    onItemClicked: (Int) -> Unit,
    expanded: Boolean,
    selectedIndex: Int,
    onExpandedChange: (Boolean) -> Unit,
    onDismissMenuView: () -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    displayName: String? = null,
    icon: Icon? = null,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
        containerColor = MaterialTheme.colorScheme.surface
    ),
    leadingIcon: (@Composable () -> Unit)? = null
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
    ) {
        OutlinedTextField(
            value = displayName ?: items[selectedIndex],
            onValueChange = {},
            modifier = modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (icon != null) {
                    val contentDescription = "Trailing icon for exposed dropdown menu"
                    IconButton(
                        onClick = { },
                        modifier = Modifier.clearAndSetSemantics { }
                    ) {
                        when (icon) {
                            is Icon.DrawableResourceIcon -> androidx.compose.material3.Icon(
                                painter = painterResource(id = icon.id),
                                contentDescription = contentDescription
                            )
                            is Icon.ImageVectorIcon -> androidx.compose.material3.Icon(
                                imageVector = icon.imageVectorIcon,
                                contentDescription = contentDescription
                            )
                        }
                    }
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            colors = colors,
            label = label
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissMenuView
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onItemClicked(index)
                        onDismissMenuView()
                    }
                )
            }
        }
    }
}
