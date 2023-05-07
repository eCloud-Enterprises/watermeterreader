// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("ecloud.android.library")
    id("ecloud.android.hilt")
    id("ecloud.android.room")
}

android {
    namespace = "com.ecloud.apps.watermeterreader.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.coroutines.android)
}