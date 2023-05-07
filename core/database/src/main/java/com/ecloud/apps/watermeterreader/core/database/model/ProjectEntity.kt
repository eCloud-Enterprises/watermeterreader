package com.ecloud.apps.watermeterreader.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ecloud.apps.watermeterreader.core.model.data.Project

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "name")
    val name: String
)

fun ProjectEntity.asExternalModel() =
    Project(code, name)
