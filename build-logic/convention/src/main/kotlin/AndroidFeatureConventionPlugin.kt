
import com.android.build.gradle.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("ecloud.android.library")
                apply("ecloud.android.hilt")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<LibraryExtension> {
//                defaultConfig {
//                    testInstrumentationRunner = "com.ecloud.apps.ebtscanner.core.testing.EbtScannerRunner"
//                }
                libraryVariants.all {
                    val variant = this
                    sourceSets {
                        getByName(variant.name) {
                            kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
                        }
                    }
                }
            }

            extensions.configure<KspExtension> {
                arg("compose-destinations.mode", "destinations")
                arg("compose-destinations.moduleName", name.replace("feature", ""))
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:designsystem"))
//                add("implementation", project(":core:data"))
                add("implementation", project(":core:common"))
//
//                add("testImplementation", kotlin("test"))
//                add("testImplementation",project(":core:testing"))
//                add("androidTestImplementation", kotlin("test"))
//                add("androidTestImplementation",project(":core:testing"))


                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("compose.destinations.animation").get())
                add("ksp", libs.findLibrary("compose.destinations.ksp").get())

                add(
                    "implementation",
                    libs.findLibrary("androidx.lifecycle-runtimeCompose").get()
                )
                add(
                    "implementation",
                    libs.findLibrary("androidx.lifecycle-viewModelCompose").get()
                )

                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }

}