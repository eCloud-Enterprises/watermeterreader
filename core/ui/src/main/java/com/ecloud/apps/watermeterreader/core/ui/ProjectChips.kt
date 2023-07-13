package com.ecloud.apps.watermeterreader.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.model.data.Project

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun ProjectChips(
    projects: List<Project>,
    onClick: (Project) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(1f)
            .wrapContentHeight(align = Alignment.Top),
        horizontalArrangement = Arrangement.Start
    ) {
        projects.forEach { project ->
            ProjectChip(
                project = project,
                onClick = onClick,
                modifier = Modifier.align(alignment = CenterVertically)
            )
        }
    }
}

@Composable
private fun ProjectChip(
    project: Project,
    onClick: (project: Project) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        var openDialog by remember { mutableStateOf(false) }
        AssistChip(
            label = { Text(text = project.name) },
            onClick = {
                openDialog = true
            },
            trailingIcon = {
                Icon(
                    imageVector = EbtIcons.FilledClose,
                    contentDescription = "Deselect Project",
                    modifier = Modifier.size(AssistChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = LocalContentAlpha.current)
                )
            },
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                title = {
                    Text(text = "Deselect Project ${project.name}")
                },
                icon = {
                    Icon(
                        imageVector = EbtIcons.FilledClose,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = LocalContentAlpha.current)
                    )
                },
                text = { Text(text = "Are you sure you want to deselect this project for download?") },
                confirmButton = {
                    TextButton(onClick = {
                        onClick(project)
                        openDialog = false
                    }) {
                        Text(text = "Deselect")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {

                        openDialog = false
                    }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }
}
