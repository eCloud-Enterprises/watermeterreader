package com.ecloud.apps.watermeterreader.feature.projects

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProjectsScreen(navigator: DestinationsNavigator) {
    ProjectsScreen()
}

@Composable
fun ProjectsScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Projects")
        }
    }

}

@Preview
@Composable
fun ProjectScreenPreview() {
    WmrTheme {
        ProjectsScreen()
    }
}