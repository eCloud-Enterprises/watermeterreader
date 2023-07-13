package com.ecloud.apps.watermeterreader.feature.auth.network

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.model.data.NetworkUrl
import com.ecloud.apps.watermeterreader.feature.auth.R

@Composable
internal fun UrlItem(
    selected: Boolean,
    url: NetworkUrl,
    onSelectUrl: (String) -> Unit,
    onNavigateToAddEdit: (name: String?, selected: Boolean) -> Unit,
    onDeleteUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { onSelectUrl(url.url) }
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = url.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = url.url,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic,
                ),
                overflow = TextOverflow.Ellipsis
            )
        }
        Row {
            IconButton(onClick = { onNavigateToAddEdit(url.name, selected) }) {
                Icon(
                    imageVector = EbtIcons.Edit,
                    contentDescription = stringResource(id = R.string.edit_url)
                )
            }
            IconButton(
                onClick = { onDeleteUrlClick(url.name) },
                enabled = !selected
            ) {
                Icon(
                    imageVector = EbtIcons.Delete,
                    contentDescription = stringResource(R.string.delete_url)
                )
            }
        }
    }
}
