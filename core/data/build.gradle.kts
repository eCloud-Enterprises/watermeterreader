plugins {
    id("ecloud.android.library")
    id("kotlinx-serialization")
    id("ecloud.android.hilt")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

//    testImplementation(project(":core:testing"))
//    testImplementation(project(":core:datastore-test"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
android {
    namespace = "com.ecloud.apps.watermeterreader.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
