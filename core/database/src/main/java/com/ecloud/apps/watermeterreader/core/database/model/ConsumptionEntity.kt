package com.ecloud.apps.watermeterreader.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ecloud.apps.watermeterreader.core.model.data.Consumption

@Entity(
    tableName = "consumptions",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = arrayOf("code"),
            childColumns = arrayOf("projectCode"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class ConsumptionEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "meter_no")
    val meterNo: String,
    @ColumnInfo(name = "current_reading")
    val currentReading: Float,
    @ColumnInfo(name = "previous_reading")
    val previousReading: Int,
    @ColumnInfo(name = "adjustments")
    val adjustments: Float,
    @ColumnInfo(name = "remarks")
    val remarks: String,
    @ColumnInfo(name = "consumption")
    val consumption: Float,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(index = true)
    val projectCode: String

)

fun ConsumptionEntity.asExternalModel() = Consumption(
    meterNo = meterNo,
    previousReading = previousReading,
    currentReading =  currentReading,
    adjustments = adjustments,
    remarks = remarks,
    consumption = consumption,
    projectCode = projectCode,
    location = location,
)