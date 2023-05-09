package com.ecloud.apps.watermeterreader.feature.onboarding.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.feature.onboarding.OnboardNavigator
import com.ecloud.apps.watermeterreader.feature.onboarding.R
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun WelcomeScreen(navigator: OnboardNavigator) {
    WelcomeScreen(onNextClick = navigator::navigateToNextScreen)
}

@Composable
internal fun WelcomeScreen(onNextClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.network_setup),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNextClick, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.next_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WmrTheme {
        WelcomeScreen(onNextClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenDarkPreview() {
    WmrTheme(darkTheme = true) {
        WelcomeScreen(onNextClick = {})
    }
}
