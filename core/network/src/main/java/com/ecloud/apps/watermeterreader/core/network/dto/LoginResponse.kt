package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val login: List<LoginDto>
)
