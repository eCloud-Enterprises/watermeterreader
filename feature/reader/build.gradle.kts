plugins {
    id("ecloud.android.library")
    id("ecloud.android.feature")
    id("ecloud.android.library.compose")
}

android {
    namespace = "com.ecloud.apps.watermeterreader.feature.reader"
}

dependencies {

    implementation(libs.accompanist.permissions)

    implementation(libs.mlkit.barcodeScanning)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
}