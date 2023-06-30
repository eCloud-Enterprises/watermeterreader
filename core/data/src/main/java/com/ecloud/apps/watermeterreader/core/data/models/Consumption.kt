package com.ecloud.apps.watermeterreader.core.data.models

import com.ecloud.apps.watermeterreader.core.database.model.WaterReadingItemEntity
import com.ecloud.apps.watermeterreader.core.model.data.WaterReadingItem
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkConsumption

fun NetworkConsumption.asEntity(projectCode: String) = WaterReadingItemEntity(
    meterNo = meterNo,
    currentReading = currentReading.toFloat(),
    previousReading = previousReading.toFloat(),
    adjustments = adjustments.toFloat(),
    remarks = remarks,
    projectCode = projectCode,
    consumption = consumption.toFloat(),
    location = stockcode,
    contract = contract
)

fun WaterReadingItem.asEntity() = WaterReadingItemEntity(
    meterNo = meterNo,
    currentReading = currentReading,
    previousReading = previousReading,
    remarks = remarks,
    adjustments = adjustments,
    projectCode = projectCode,
    consumption = consumption,
    location = location,
    contract = contract
)