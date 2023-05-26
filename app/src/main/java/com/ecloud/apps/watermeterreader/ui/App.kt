package com.ecloud.apps.watermeterreader.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ecloud.apps.watermeterreader.bottombar.BottomBarDestination
import com.ecloud.apps.watermeterreader.navigation.CoreFeatureNavigator
import com.ecloud.apps.watermeterreader.navigation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.Route
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(startRoute: Route) {
    val appState = rememberAppState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) },
        bottomBar = {
            val current by appState.navController.currentDestinationAsState()
            val showBottomBar = current in BottomBarDestination.values().map { it.direction }
            if (showBottomBar) {
                BottomBar(selectedNavigation = current, onNavigationSelected = { selected ->
                    appState.navController.navigate(selected) {
                        launchSingleTop = true
                        restoreState = true

                        popUpTo(appState.navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                })

            }
        },
    ) { padding ->
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
