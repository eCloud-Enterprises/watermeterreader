package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Synchronizer
import com.ecloud.apps.watermeterreader.core.data.models.asEntity
import com.ecloud.apps.watermeterreader.core.database.dao.ProjectDao
import com.ecloud.apps.watermeterreader.core.model.data.WaterReadingItem
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OfflineFirstConsumptionRepository @Inject constructor(
    private val network: WmrNetworkDataSource,
    private val projectDao: ProjectDao
) : ConsumptionRepository {
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        // get the list of projects
        val projects = projectDao.getProjectsStream().first()
        val projectCodes = projects.map { it.code }
        fetchConsumptions(projectCodes)
        return true
    }

    override fun updateConsumption(waterReadingItem: WaterReadingItem) {
        projectDao.updateConsumption(waterReadingItem.asEntity())
    }

    override suspend fun fetchConsumptions(projectCodes: List<String>) {
        val consumptionEntities = projectCodes.pmap { code ->
            network.getConsumptions(code).map { c -> c.asEntity(code) }
        }.flatten()

        projectDao.upsertConsumptions(consumptionEntities)
        projectDao.updateProjectsDownloaded(projectCodes)

    }
}

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}