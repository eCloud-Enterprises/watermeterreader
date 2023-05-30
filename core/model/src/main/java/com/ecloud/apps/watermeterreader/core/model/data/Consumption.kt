package com.ecloud.apps.watermeterreader.core.model.data

data class Consumption(
    val meterNo: String,
    val previousReading: Int,
    val currentReading: Float,
    val adjustments: Float,
    val remarks: String,
    val consumption: Float,
    val projectCode: String,
    val location: String,
)

fun Consumption.getDisplayName(): String = "$meterNo - $location"