package com.ecloud.apps.watermeterreader.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ecloud.apps.watermeterreader.core.model.data.Branch

@Entity(tableName = "branches")
data class BranchEntity(
    @PrimaryKey(autoGenerate = false)
    val code: String,
    val name: String
)

fun BranchEntity.asExternalModel(): Branch = Branch(code, name)
