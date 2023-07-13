package com.ecloud.apps.watermeterreader.navigation

import com.ecloud.apps.watermeterreader.feature.auth.destinations.LoginScreenDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.NetworkScreenDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.WelcomeScreenDestination
import com.ecloud.apps.watermeterreader.feature.projects.destinations.ProjectsScreenDestination
import com.ecloud.apps.watermeterreader.feature.reader.destinations.ProjectSelectScreenDestination
import com.ecloud.apps.watermeterreader.feature.reader.destinations.ReaderScreenDestination
import com.ecloud.apps.watermeterreader.feature.settings.destinations.SettingsScreenDestination
import com.ecloud.apps.watermeterreader.feature.upload.destinations.UploadScreenDestination
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

    val auth = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = listOf(
                LoginScreenDestination,
                NetworkScreenDestination
            ).associateBy { it.route }
        override val route: String
            get() = "auth"
        override val startRoute: Route
            get() = LoginScreenDestination
    }

    val reader = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = listOf(
                ReaderScreenDestination,
                UploadScreenDestination,
                SettingsScreenDestination,
                ProjectsScreenDestination,
                ProjectSelectScreenDestination
            ).associateBy { it.route }
        override val route: String
            get() = "reader"
        override val startRoute: Route
            get() = ReaderScreenDestination
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
                onboarding,
                auth,
                reader
            )

    }
}