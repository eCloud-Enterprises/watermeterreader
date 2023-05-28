package com.ecloud.apps.watermeterreader.core.data.repository

import android.util.Log
import com.ecloud.apps.watermeterreader.core.data.Synchronizer
import com.ecloud.apps.watermeterreader.core.data.models.asEntity
import com.ecloud.apps.watermeterreader.core.database.dao.ProjectDao
import com.ecloud.apps.watermeterreader.core.database.model.ConsumptionEntity
import com.ecloud.apps.watermeterreader.core.model.data.Consumption
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
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

    override fun updateConsumption(consumption: Consumption) {
        consumption.run {
            projectDao.updateConsumption(
                ConsumptionEntity(
                    meterNo,
                    currentReading,
                    previousReading = previousReading,
                    remarks = remarks,
                    adjustments = adjustments,
                    projectCode = projectCode,
                    consumption = this.consumption
                )
            )
        }
    }

    override suspend fun fetchConsumptions(projectCodes: List<String>) {
        val consumptionEntities = projectCodes.pmap { code ->
            network.getConsumptions(code).map { c -> c.asEntity(code) }
        }.flatten()

        Log.d("ConsumptionRepo", consumptionEntities.map { it.meterNo }.toString())


         projectDao.upsertConsumptions(consumptionEntities)

    }
}

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}