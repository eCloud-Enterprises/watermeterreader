package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Syncable
import com.ecloud.apps.watermeterreader.core.model.data.Consumption
import kotlinx.coroutines.flow.Flow

interface ConsumptionRepository : Syncable {


    suspend fun fetchConsumptions(projectCodes: List<String>)

    fun updateConsumption(consumption: Consumption)
}