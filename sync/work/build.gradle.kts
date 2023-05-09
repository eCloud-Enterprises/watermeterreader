plugins {
    id("ecloud.android.library")
    id("ecloud.android.hilt")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.ecloud.apps.watermeterreader.sync"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.work)
    implementation(libs.hilt.ext.work)
}