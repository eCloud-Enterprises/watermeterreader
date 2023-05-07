package com.ecloud.apps.watermeterreader

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType,
}

@Suppress("EnumEntryName")
enum class WaterMeterReaderFlavor(
    val dimension: FlavorDimension,
    val applicationSuffixId: String? = null
) {
    demo(FlavorDimension.contentType, applicationSuffixId = ".demo"),
    prod(FlavorDimension.contentType),
}

@Suppress("unused")
fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: WaterMeterReaderFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.contentType.name
        productFlavors {
            WaterMeterReaderFlavor.values().forEach {
                create(it.name) {
                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (it.applicationSuffixId != null) {
                            this.applicationIdSuffix = it.applicationSuffixId
                        }
                    }
                }
            }
        }
    }
}