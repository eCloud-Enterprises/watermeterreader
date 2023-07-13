package com.ecloud.apps.watermeterreader.feature.upload

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
fun UploadScreen(navigator: DestinationsNavigator) {
    UploadScreen()
}

@Composable
fun UploadScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Upload")
        }
    }

}

@Preview
@Composable
fun UploadScreenPreview() {
    WmrTheme {
        UploadScreen()
    }
}
