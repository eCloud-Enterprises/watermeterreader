import com.ecloud.apps.watermeterreader.WaterMeterReaderBuildType

plugins {
    id("ecloud.android.application")
    id("ecloud.android.application.compose")
    id("ecloud.android.application.flavors")
    id("ecloud.android.hilt")
    id("ecloud.android.application.firebase")
}

android {
    namespace = "com.ecloud.apps.watermeterreader"

    defaultConfig {
        applicationId = "com.ecloud.apps.watermeterreader"
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = WaterMeterReaderBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = false
            applicationIdSuffix = WaterMeterReaderBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":sync:work"))

    implementation(project(":feature:onboarding"))
    implementation(project(":feature:auth"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.window)

    implementation(libs.compose.destinations.animation)

}

configurations.configureEach {
    resolutionStrategy {
        force(libs.junit4)
    }
}
