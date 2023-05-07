
plugins {
    id("ecloud.android.library")
    id("kotlinx-serialization")
    id("ecloud.android.hilt")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
}
android {
    namespace = "com.ecloud.apps.watermeterreader.core.network"
}
