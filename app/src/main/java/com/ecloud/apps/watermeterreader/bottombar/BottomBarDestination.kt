package com.ecloud.apps.watermeterreader.bottombar

import androidx.annotation.StringRes
import com.ecloud.apps.watermeterreader.R
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.designsystem.icon.Icon
import com.ecloud.apps.watermeterreader.feature.projects.destinations.ProjectsScreenDestination
import com.ecloud.apps.watermeterreader.feature.reader.destinations.ReaderScreenDestination
import com.ecloud.apps.watermeterreader.feature.settings.destinations.SettingsScreenDestination
import com.ecloud.apps.watermeterreader.feature.upload.destinations.UploadScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestination(
    @StringRes val title: Int,
    val direction: DirectionDestinationSpec,
    val icon: Icon
) {

    Home(
        R.string.text_home,
        ReaderScreenDestination,
        Icon.ImageVectorIcon(EbtIcons.Home)
    ),
    Download(
        R.string.text_download,
        ProjectsScreenDestination,
        Icon.DrawableResourceIcon(EbtIcons.Download)
    ),
    Upload(
        R.string.text_upload,
        UploadScreenDestination,
        Icon.DrawableResourceIcon(EbtIcons.CloudUpload)
    ),
    Settings(
        R.string.text_settings,
        SettingsScreenDestination,
        Icon.ImageVectorIcon(EbtIcons.Settings)
    )

}
