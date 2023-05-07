package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WarehouseDto(
    @SerialName("warehousecode")
    val warehousecode: String,
    @SerialName("warehousename")
    val warehousename: String
)
