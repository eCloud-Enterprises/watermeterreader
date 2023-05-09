package com.ecloud.apps.watermeterreader.core.data.repository

import kotlinx.coroutines.flow.Flow

class AuthenticationFailed(override val message: String) : Exception()

interface AuthRepository {

    suspend fun login(username: String, password: String)

    fun authenticate(): Flow<Unit>

    suspend fun logout()
}
