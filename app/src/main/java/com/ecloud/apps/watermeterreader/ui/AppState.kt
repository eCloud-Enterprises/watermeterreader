package com.ecloud.apps.watermeterreader.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ecloud.apps.watermeterreader.core.ui.JankMetricDisposableEffect
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine

class AppState(
    val navController: NavHostController,
    val engine: NavHostEngine,
    val snackbarHostState: SnackbarHostState
)

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
