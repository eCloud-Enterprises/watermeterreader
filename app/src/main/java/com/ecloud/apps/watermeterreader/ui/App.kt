package com.ecloud.apps.watermeterreader.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ecloud.apps.watermeterreader.navigation.CoreFeatureNavigator
import com.ecloud.apps.watermeterreader.navigation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(startRoute: Route) {
    val appState = rememberAppState()
    Scaffold(snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) }) { padding ->
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            startRoute = startRoute,
            modifier = Modifier.padding(padding),
            engine = appState.engine,
            navController = appState.navController,
            dependenciesContainerBuilder = {
                dependency(appState.snackbarHostState)
                dependency(CoreFeatureNavigator(destination, navController))
            }
        )

    }
}
