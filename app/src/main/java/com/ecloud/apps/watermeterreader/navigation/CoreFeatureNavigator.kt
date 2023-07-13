package com.ecloud.apps.watermeterreader.navigation

import androidx.navigation.NavController
import com.ecloud.apps.watermeterreader.feature.auth.AuthNavigator
import com.ecloud.apps.watermeterreader.feature.auth.addediturl.AddEditUrlRouteNavArgs
import com.ecloud.apps.watermeterreader.feature.auth.destinations.AddEditUrlScreenDestination
import com.ecloud.apps.watermeterreader.feature.auth.destinations.LoginScreenDestination
import com.ecloud.apps.watermeterreader.feature.auth.destinations.NetworkSettingsScreenDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.OnboardNavigator
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.NetworkScreenDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.OnboardingDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationSpec

class CoreFeatureNavigator(
    private val currentDestination: DestinationSpec<*>,
    private val navController: NavController
) : OnboardNavigator, AuthNavigator {
    override fun navigateToNextScreen() {
        currentDestination as? OnboardingDestination
            ?: throw RuntimeException("Trying to use Onboarding navigator from a non onboarding screen")
        val nextDirection = when (currentDestination) {
            NetworkScreenDestination -> NavGraphs.auth
            WelcomeScreenDestination -> NetworkScreenDestination
        }

        navController.navigate(nextDirection)
    }

    override fun navigateToNetworkSettings() {
        navController.navigate(NetworkSettingsScreenDestination)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToAddEditForm(args: AddEditUrlRouteNavArgs) {
        navController.navigate(AddEditUrlScreenDestination(args))
    }
}