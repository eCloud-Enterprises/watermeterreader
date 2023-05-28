package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName

data class GetConsumptionsResponse(
    @SerialName("consumptions")
    val consumptions: List<NetworkConsumption>
)
