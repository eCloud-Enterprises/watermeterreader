package com.ecloud.apps.watermeterreader.feature.auth.addediturl

import com.ecloud.apps.watermeterreader.core.model.data.NetworkUrl

sealed class AddEditUrlEvent {
    data class OnSubmit(val networkUrl: NetworkUrl): AddEditUrlEvent()

    object OnSnackbarShown: AddEditUrlEvent()
}
