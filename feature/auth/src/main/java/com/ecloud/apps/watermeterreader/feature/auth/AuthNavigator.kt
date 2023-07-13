package com.ecloud.apps.watermeterreader.feature.auth

import com.ecloud.apps.watermeterreader.feature.auth.addediturl.AddEditUrlRouteNavArgs

interface AuthNavigator {

    fun navigateToNetworkSettings()

    fun navigateUp()

    fun navigateToAddEditForm(args: AddEditUrlRouteNavArgs)

}