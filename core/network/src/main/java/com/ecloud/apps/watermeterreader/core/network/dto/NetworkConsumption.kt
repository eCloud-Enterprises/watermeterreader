package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkConsumption(
   @SerialName("meterno")
   val  meterNo: String,
   @SerialName("prevreading")
   val  previousReading: Int,
   @SerialName("currentreading")
   val currentReading: Float,
   @SerialName("adjustments")
   val adjustments: Float,
   @SerialName("remarks")
   val remarks: String,
   @SerialName("consumption")
   val consumption: Float,
)
