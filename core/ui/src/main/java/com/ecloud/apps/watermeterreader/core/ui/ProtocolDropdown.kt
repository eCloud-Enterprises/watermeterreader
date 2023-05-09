package com.ecloud.apps.watermeterreader.core.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ecloud.apps.watermeterreader.core.designsystem.components.EbtDropdown

@Composable
fun ProtocolDropdown(
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<String> = listOf("http", "https")
) {
    var expanded by remember { mutableStateOf(false) }

    EbtDropdown(
        items = items,
        onItemClicked = { onItemClick(it) },
        expanded = expanded,
        selectedIndex = selectedIndex,
        onExpandedChange = { expanded = !expanded },
        onDismissMenuView = { expanded = false },
        modifier = modifier,
        label = { Text(text = stringResource(R.string.label_protocol)) }
    )
}
