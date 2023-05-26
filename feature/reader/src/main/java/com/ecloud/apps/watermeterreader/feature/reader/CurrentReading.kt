package com.ecloud.apps.watermeterreader.feature.reader

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrentReading(
    modifier: Modifier = Modifier,
    value: String = "",
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(text = stringResource(R.string.text_current_reading)) },
        modifier = modifier
            .fillMaxWidth(),
        readOnly = true
    )
}
