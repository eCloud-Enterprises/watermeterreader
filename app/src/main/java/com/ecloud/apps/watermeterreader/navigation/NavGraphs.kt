package com.ecloud.apps.watermeterreader.navigation

import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.NetworkScreenDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

object NavGraphs {

    val onboarding = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = listOf(
                WelcomeScreenDestination,
                NetworkScreenDestination
            ).associateBy { it.route }
        override val route: String
            get() = "onboarding"
        override val startRoute: Route
            get() = WelcomeScreenDestination
    }

    val root = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = emptyMap()
        override val route: String
            get() = "root"
        override val startRoute: Route
            get() = onboarding
        override val nestedNavGraphs: List<NavGraphSpec>
            get() = listOf(
                onboarding
            )

    }
}