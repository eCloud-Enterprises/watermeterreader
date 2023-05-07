package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.datastore.WmrPreferencesDataSource
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import java.math.BigInteger
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

private fun String.toMd5(): String {
    val md = java.security.MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}

class OfflineFirstAuthRepository @Inject constructor(
    private val wmrNetworkDataSource: WmrNetworkDataSource,
    private val dataSource: WmrPreferencesDataSource,
) : AuthRepository {
    override suspend fun login(username: String, password: String) {

        val response =
            wmrNetworkDataSource.login(username, password.toMd5())

        response.let { loginResponse ->
            if (loginResponse.returnstatus == "Invalid User ID/Password!") {
                throw AuthenticationFailed(loginResponse.returnstatus)
            }

            loginResponse.run {
                dataSource.setUserData(
                    userId = userid.orEmpty(),
                    branchCode = branchcode.orEmpty(),
                    branchName = branchname.orEmpty(),
                    companyCode = companycode.orEmpty()
                )
            }
        }
    }

    override fun authenticate(): Flow<Unit> {
        return flow {
            val userData = dataSource.userDataStream.catch { exception ->
                if (exception is java.io.IOException) {
                    throw Error("Couldn't load data")
                } else {
                    throw exception
                }
            }

            userData.let {
                if (it.first().id.isEmpty()) {
                    throw Error("User not authenticated")
                }

                emit(Unit)
            }
        }
    }

    override suspend fun logout() {
        dataSource.removeUserData()
    }
}
