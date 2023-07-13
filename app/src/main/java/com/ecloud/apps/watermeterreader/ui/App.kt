package com.ecloud.apps.watermeterreader.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ecloud.apps.watermeterreader.core.designsystem.components.EbtTopAppBar
import com.ecloud.apps.watermeterreader.feature.projects.destinations.ProjectsScreenDestination
import com.ecloud.apps.watermeterreader.feature.reader.destinations.ProjectSelectScreenDestination
import com.ecloud.apps.watermeterreader.navigation.CoreFeatureNavigator
import com.ecloud.apps.watermeterreader.navigation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(startRoute: Route) {
    val appState = rememberAppState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                BottomBar(
                    destinations = appState.topLevelDestinations,
                    currentDestination = appState.currentDestination,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                )
            }
        },
        topBar = {
            // show the top appbar on top level destinations
            val destination = appState.currentTopLevelDestination
            if (destination != null) {
                EbtTopAppBar(titleRes = destination.title)
            }
        },
        floatingActionButton = {
            if (appState.currentDestination == ProjectsScreenDestination) {
                ExtendedFloatingActionButton(
                    onClick = { appState.navController.navigate(ProjectSelectScreenDestination) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = ""
                        )
                    },
                    text = { Text(text = "Download Project") },
                )
            }
        }
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
