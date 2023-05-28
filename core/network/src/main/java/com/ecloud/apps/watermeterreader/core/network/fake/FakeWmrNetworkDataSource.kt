package com.ecloud.apps.watermeterreader.core.network.fake

import JvmUnitTestFakeAssetManager
import com.ecloud.apps.watermeterreader.core.network.Dispatcher
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.WmrDispatcher
import com.ecloud.apps.watermeterreader.core.network.dto.LoginDto
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkBranch
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkConsumption
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkProject
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

/**
 * [WmrNetworkDataSource] implementation that provides static resources to aid development
 */
class FakeWmrNetworkDataSource @Inject constructor(
    @Dispatcher(WmrDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = JvmUnitTestFakeAssetManager
) : WmrNetworkDataSource {

    companion object {
        private const val WAREHOUSES_ASSET = "projects.json"
        private const val LOGIN_ASSET = "login.json"
        private const val BRANCHES_ASSET = "branches.json"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getProjects(objectCode: String, type: String): List<NetworkProject> =
        withContext(ioDispatcher) {
            assets.open(WAREHOUSES_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun getConsumptions(projectCode: String): List<NetworkConsumption> =
        withContext(ioDispatcher){
            assets.open("consumption-$projectCode.json").use(networkJson::decodeFromStream)
        }

    override suspend fun login(userid: String, password: String): LoginDto =
        withContext(ioDispatcher) {
            assets.open(LOGIN_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun getBranches(objectCode: String, type: String): List<NetworkBranch> =
        withContext(ioDispatcher) {
            assets.open(BRANCHES_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun checkProjects(): Boolean = withContext(ioDispatcher) { true }

    override suspend fun checkStockAudits(): Boolean = withContext(ioDispatcher) { true }
}
