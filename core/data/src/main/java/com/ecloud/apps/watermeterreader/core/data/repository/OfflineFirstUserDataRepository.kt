package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.datastore.WmrPreferencesDataSource
import com.ecloud.apps.watermeterreader.core.model.data.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class OfflineFirstUserDataRepository @Inject constructor(
    private val wmrPreferencesDataSource: WmrPreferencesDataSource
) : UserDataRepository {
    override val userDataStream: Flow<UserData>
        get() = wmrPreferencesDataSource.userDataStream

    override suspend fun updateUrlByName(name: String, url: String) =
        wmrPreferencesDataSource.updateUrls(name, url)

    override suspend fun setUserData(
        userId: String,
        branchCode: String,
        branchName: String,
        companyCode: String
    ) = wmrPreferencesDataSource.setUserData(userId, branchCode, branchName, companyCode)

    override suspend fun setBranch(branchCode: String, branchName: String) =
        wmrPreferencesDataSource.setBranch(branchCode, branchName)

    override suspend fun setProjectCode(warehouseCode: String) =
        wmrPreferencesDataSource.setProjectCode(warehouseCode)

    override suspend fun toggleShouldUpdateAuditList(value: Boolean) =
        wmrPreferencesDataSource.toggleShouldUpdateAuditList(value)

    override suspend fun clearUrl() = wmrPreferencesDataSource.clearBaseUrl()
    override suspend fun toggleMode(value: Boolean) =
        wmrPreferencesDataSource.toggleMode(value)

    override suspend fun deleteUrlByKey(key: String) =
        wmrPreferencesDataSource.deleteUrlByKey(key)

    override suspend fun setUrl(url: String) = wmrPreferencesDataSource.setBaseUrl(url)

    override suspend fun addUrl(name: String, url: String) =
        wmrPreferencesDataSource.addUrl(name, url)
}
