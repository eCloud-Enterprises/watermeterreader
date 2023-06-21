package com.ecloud.apps.watermeterreader.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.tracing.trace
import com.ecloud.apps.watermeterreader.bottombar.BottomBarDestination
import com.ecloud.apps.watermeterreader.core.ui.JankMetricDisposableEffect
import com.ecloud.apps.watermeterreader.feature.projects.destinations.ProjectsScreenDestination
import com.ecloud.apps.watermeterreader.feature.reader.destinations.ReaderScreenDestination
import com.ecloud.apps.watermeterreader.feature.settings.destinations.SettingsScreenDestination
import com.ecloud.apps.watermeterreader.feature.upload.destinations.UploadScreenDestination
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@Stable
class AppState(
    val navController: NavHostController,
    val engine: NavHostEngine,
    val snackbarHostState: SnackbarHostState
) {
    val topLevelDestinations = BottomBarDestination.values().asList()
    val currentDestination
        @Composable get() = navController.currentDestinationAsState().value

    val currentTopLevelDestination
        @Composable get() = when (currentDestination) {
            ReaderScreenDestination -> BottomBarDestination.Home
            ProjectsScreenDestination -> BottomBarDestination.Download
            UploadScreenDestination -> BottomBarDestination.Upload
            SettingsScreenDestination -> BottomBarDestination.Settings
            else -> null
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination in BottomBarDestination.values().map { it.direction }

    fun navigateToTopLevelDestination(topLevelDestination: BottomBarDestination) {
        trace("Navigation: ${topLevelDestination}") {
            navController.navigate(topLevelDestination.direction) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun rememberAppState(
    navController: NavHostController = rememberAnimatedNavController(),
    engine: NavHostEngine = rememberAnimatedNavHostEngine(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): AppState {
    NavigationTrackingSideEffect(navController)
    return remember(navController, engine, snackbarHostState) {
        AppState(navController, engine, snackbarHostState)
    }

}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    JankMetricDisposableEffect(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
