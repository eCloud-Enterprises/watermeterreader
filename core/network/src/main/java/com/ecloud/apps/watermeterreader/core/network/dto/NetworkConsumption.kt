package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkConsumption(
    @SerialName("contract")
    val contract: String,
    @SerialName("stockcode")
    val stockcode: String,
    @SerialName("meterno")
    val meterNo: String,
    @SerialName("previousreading")
    val previousReading: String,
    @SerialName("currentreading")
    val currentReading: String,
    @SerialName("adjustment")
    val adjustments: String,
    @SerialName("adjustmentremarks")
    val remarks: String,
    @SerialName("consumption")
    val consumption: String,
)
