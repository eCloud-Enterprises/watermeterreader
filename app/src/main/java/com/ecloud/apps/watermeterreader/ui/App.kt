package com.ecloud.apps.watermeterreader.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.WelcomeScreenDestination
import com.ecloud.apps.watermeterreader.navigation.CommonNavGraphNavigator
import com.ecloud.apps.watermeterreader.navigation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val appState = rememberAppState()
    Scaffold(snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) }) { padding ->
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            startRoute = NavGraphs.onboarding,
            modifier = Modifier.padding(padding),
            engine = appState.engine,
            navController = appState.navController,
            dependenciesContainerBuilder = {
                dependency(appState.snackbarHostState)
                dependency(CommonNavGraphNavigator(destination, navController))
            }
        )

    }
}
