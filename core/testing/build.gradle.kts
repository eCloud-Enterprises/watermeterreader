plugins {
    id("ecloud.android.library")
    id("ecloud.android.library.compose")
    id("ecloud.android.hilt")
}

android {
    namespace = "com.ecloud.apps.watermeterreader.core.testing"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    api(libs.junit4)
    api(libs.androidx.test.core)
    api(libs.kotlinx.coroutines.test)

    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.rules)
    api(libs.androidx.compose.ui.test)
    api(libs.hilt.testing)

    debugApi(libs.androidx.compose.ui.testManifest)
}
