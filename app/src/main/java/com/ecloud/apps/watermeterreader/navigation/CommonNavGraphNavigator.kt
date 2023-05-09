package com.ecloud.apps.watermeterreader.navigation

import androidx.navigation.NavController
import com.ecloud.apps.watermeterreader.feature.onboarding.OnboardNavigator
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.NetworkScreenDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.OnboardingDestination
import com.ecloud.apps.watermeterreader.feature.onboarding.destinations.WelcomeScreenDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationSpec

class CommonNavGraphNavigator(
    private val currentDestination: DestinationSpec<*>,
    private val navController: NavController
) : OnboardNavigator {
    override fun navigateToNextScreen() {
        currentDestination as? OnboardingDestination
            ?: throw RuntimeException("Trying to use Onboarding navigator from a non onboarding screen")
        val nextDirection = when (currentDestination) {
            NetworkScreenDestination -> TODO()
            WelcomeScreenDestination -> NetworkScreenDestination
        }

        navController.navigate(nextDirection)
    }
}