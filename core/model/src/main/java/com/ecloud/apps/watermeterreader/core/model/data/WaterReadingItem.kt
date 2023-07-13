package com.ecloud.apps.watermeterreader.core.model.data

data class WaterReadingItem(
    val meterNo: String,
    val previousReading: Float,
    val currentReading: Float,
    val adjustments: Float,
    val remarks: String,
    val consumption: Float,
    val projectCode: String,
    val location: String,
    val contract: String,
)

fun WaterReadingItem.getDisplayName(): String = "$meterNo - $location"