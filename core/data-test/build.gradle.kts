plugins {
    id("ecloud.android.library")
    id("ecloud.android.hilt")
}

android {
    namespace = "com.ecloud.apps.watermeterreader.core.data.test"
}

dependencies {
    api(project(":core:data"))
    implementation(project(":core:testing"))
}