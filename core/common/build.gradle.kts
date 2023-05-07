plugins {
    id("ecloud.android.library")
    id("ecloud.android.hilt")
}

dependencies {

    implementation(libs.kotlinx.coroutines.android)
}
android {
    namespace = "com.ecloud.apps.watermeterreader.core"
}
