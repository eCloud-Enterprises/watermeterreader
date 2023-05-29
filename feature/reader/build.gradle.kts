plugins {
    id("ecloud.android.library")
    id("ecloud.android.feature")
    id("ecloud.android.library.compose")
}

android {
    namespace = "com.ecloud.apps.watermeterreader.feature.reader"
}

dependencies {

    implementation(libs.play.services.code.scanner)
    implementation(libs.play.services.base)
    implementation(libs.play.services.tflite.java)

}