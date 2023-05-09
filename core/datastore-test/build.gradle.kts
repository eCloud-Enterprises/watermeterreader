plugins {
    id("ecloud.android.library")
    id("ecloud.android.hilt")
}

android {
    namespace = "com.ecloud.apps.watermeterreader.core.datastore.test"
}

dependencies {
    api(project(":core:datastore"))
    implementation(project(":core:testing"))

    api(libs.androidx.dataStore.core)
}