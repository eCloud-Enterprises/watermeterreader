package com.ecloud.apps.watermeterreader.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.ecloud.apps.watermeterreader.core.model.data.WaterReadingItem

@Entity(
    tableName = "water_reading_item",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = arrayOf("code"),
            childColumns = arrayOf("projectCode"),
            onDelete = ForeignKey.CASCADE
        ),
    ],
    primaryKeys = ["contract", "meter_no"],
)
data class WaterReadingItemEntity(
    @ColumnInfo(name = "contract")
    val contract: String,
    @ColumnInfo(name = "meter_no")
    val meterNo: String,
    @ColumnInfo(name = "current_reading")
    val currentReading: Float,
    @ColumnInfo(name = "previous_reading")
    val previousReading: Float,
    @ColumnInfo(name = "adjustments")
    val adjustments: Float,
    @ColumnInfo(name = "remarks")
    val remarks: String,
    @ColumnInfo(name = "consumption")
    val consumption: Float,
    @ColumnInfo(name = "stockcode")
    val location: String,
    @ColumnInfo(index = true)
    val projectCode: String

)

fun WaterReadingItemEntity.asExternalModel() = WaterReadingItem(
    meterNo = meterNo,
    previousReading = previousReading,
    currentReading = currentReading,
    adjustments = adjustments,
    remarks = remarks,
    consumption = consumption,
    projectCode = projectCode,
    location = location,
    contract = contract
)