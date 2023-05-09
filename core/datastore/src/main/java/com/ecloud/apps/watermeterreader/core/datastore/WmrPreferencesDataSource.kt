package com.ecloud.apps.watermeterreader.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.ecloud.apps.watermeterreader.core.model.data.NetworkUrl
import com.ecloud.apps.watermeterreader.core.model.data.UserData
import com.google.protobuf.kotlin.DslList
import com.google.protobuf.kotlin.DslProxy
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class WmrPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {

    val userDataStream = userPreferences.data.map {
        UserData(
            id = it.userId,
            branchName = it.branchName,
            branchCode = it.branchCode,
            companyCode = it.companyCode,
            projectCode = it.projectCode,
            selectedUrl = it.baseUrl,
            customUrl = it.customUrlsList.map { url -> NetworkUrl(url.name, url.url) },
            shouldUpdateAuditList = it.shouldRefetchList,
            onlineMode = it.onlineMode
        )
    }

    suspend fun setBranch(code: String, name: String) {
        userPreferences.updateData {
            it.copy {
                branchCode = code
                branchName = name
            }
        }
    }

    suspend fun setUserData(
        userId: String,
        branchCode: String,
        branchName: String,
        companyCode: String
    ) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.branchCode = branchCode
                    this.userId = userId
                    this.branchName = branchName
                    this.companyCode = companyCode
                }
            }
        } catch (ioException: IOException) {
            Log.e("EbtScannerPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun removeUserData() {
        userPreferences.updateData {
            it.copy {
                this.userId = ""
            }
        }
    }

    suspend fun toggleShouldUpdateAuditList(value: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.shouldRefetchList = value
            }
        }
    }

    suspend fun addUrl(name: String, url: String) {
        userPreferences.updateData {
            it.copy {
                customUrls.add(UserPreferences.Url.newBuilder().setName(name).setUrl(url).build())
            }
        }
    }

    suspend fun setBaseUrl(url: String) {
        userPreferences.updateData {
            it.copy {
                this.baseUrl = url
            }
        }
    }

    suspend fun clearBaseUrl() {
        userPreferences.updateData {
            it.copy { clearBaseUrl() }
        }
    }

    suspend fun toggleMode(value: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.onlineMode = value
            }
        }
    }

    suspend fun updateUrls(name: String, url: String) {
        userPreferences.updateData { preferences ->
            preferences.copy {
                customUrls[preferences.customUrlsList.indexOfFirst { it.name == name }] =
                    UserPreferences.Url.newBuilder().setName(name).setUrl(url).build()
            }
        }
    }

    suspend fun deleteUrlByKey(key: String) {
        userPreferences.updateData { preferences ->
            preferences.toBuilder()
                .removeCustomUrls(preferences.customUrlsList.indexOfFirst { it.name == key })
                .build()
        }
    }

    suspend fun setProjectCode(projectCode: String) {
        userPreferences.updateData {
            it.copy {
                this.projectCode = projectCode
            }
        }
    }

    /**
     * Adds or removes [value] from the [DslList] provided by [listGetter]
     */
    private suspend fun <T : DslProxy> DataStore<UserPreferences>.editList(
        add: Boolean,
        value: String,
        listGetter: (UserPreferencesKt.Dsl) -> DslList<String, T>,
        clear: UserPreferencesKt.Dsl.(DslList<String, T>) -> Unit,
        addAll: UserPreferencesKt.Dsl.(DslList<String, T>, Iterable<String>) -> Unit
    ) {
        setList(
            listGetter = listGetter,
            listModifier = { currentList ->
                if (add) currentList + value
                else currentList - value
            },
            clear = clear,
            addAll = addAll
        )
    }

    /**
     * Sets the value provided by [listModifier] into the [DslList] read by [listGetter]
     */
    private suspend fun <T : DslProxy> DataStore<UserPreferences>.setList(
        listGetter: (UserPreferencesKt.Dsl) -> DslList<String, T>,
        listModifier: (DslList<String, T>) -> List<String>,
        clear: UserPreferencesKt.Dsl.(DslList<String, T>) -> Unit,
        addAll: UserPreferencesKt.Dsl.(DslList<String, T>, List<String>) -> Unit
    ) {
        try {
            updateData {
                it.copy {
                    val dslList = listGetter(this)
                    val newList = listModifier(dslList)
                    clear(dslList)
                    addAll(dslList, newList)
                }
            }
        } catch (ioException: IOException) {
            Log.e("EbtScannerPreferences", "Failed to update user preferences", ioException)
        }
    }
}
