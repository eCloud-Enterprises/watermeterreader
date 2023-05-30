package com.ecloud.apps.watermeterreader.core.data.models

import com.ecloud.apps.watermeterreader.core.database.model.ConsumptionEntity
import com.ecloud.apps.watermeterreader.core.model.data.Consumption
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkConsumption

fun NetworkConsumption.asEntity(projectCode: String) = ConsumptionEntity(
    meterNo = meterNo,
    currentReading = currentReading,
    previousReading = previousReading,
    adjustments = adjustments,
    remarks = remarks,
    projectCode = projectCode,
    consumption = consumption,
    location = location
)

fun Consumption.asEntity() = ConsumptionEntity(
    meterNo = meterNo,
    currentReading = currentReading,
    previousReading = previousReading,
    remarks = remarks,
    adjustments = adjustments,
    projectCode = projectCode,
    consumption = consumption,
    location = location
)