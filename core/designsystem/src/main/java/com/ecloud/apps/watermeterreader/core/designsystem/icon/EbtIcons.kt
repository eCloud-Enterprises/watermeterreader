package com.ecloud.apps.watermeterreader.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.ecloud.apps.watermeterreader.core.designsystem.R

/**
 * Ebt Scanner icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object EbtIcons {
    val Person = Icons.Default.Person
    val VisibilityOff = R.drawable.ic_visibility_off
    val Visibility = R.drawable.ic_visibility
    val ArrowBack = Icons.Default.ArrowBack
    val FlipCameraAndroid = R.drawable.ic_flip_camera_android
    val BarcodeScan = R.drawable.ic_barcode_scan
    val QrCodeScanner = R.drawable.ic_baseline_qr_code_scanner_24
    val CloudUpload = R.drawable.ic_cloud_upload
    val FlashOff = R.drawable.ic_flash_off
    val FlashOn = R.drawable.ic_flash_on
    val History = R.drawable.ic_history
    val MenuOpen = R.drawable.ic_menu_open
    val Warehouse = R.drawable.ic_warehouse
    val ZoomIn = R.drawable.ic_zoom_in
    val ZoomOut = R.drawable.ic_zoom_out
    val Add = Icons.Default.Add
    val Minus = R.drawable.ic_remove
    val Check = Icons.Default.Check
    val Settings = Icons.Default.Settings
    val Sync = R.drawable.ic_sync
    val Backspace = R.drawable.ic_backspace
    val Info = Icons.Default.Info
    val VerticalMenu = Icons.Default.MoreVert
    val Edit = Icons.Default.Edit
    val Close = Icons.Default.Close
    val Search = Icons.Default.Search
    val Filter = R.drawable.ic_filter_list
    val NetworkWifi = R.drawable.ic_network_wifi
    val Delete = Icons.Default.Delete
}

sealed class Icon {
    data class ImageVectorIcon(val imageVectorIcon: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
