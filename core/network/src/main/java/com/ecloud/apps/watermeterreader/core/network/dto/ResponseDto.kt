package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto(
    val note: String?,
    @SerialName("returnstatus")
    val returnStatus: String
)
