plugins {
    id("ecloud.android.library")
    id("ecloud.android.library.compose")
}

android {
 lint {
     checkDependencies = true
 }
    namespace = "com.ecloud.apps.watermeterreader.core.designsystem"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.material)

//    lintPublish(project(":lint"))
}