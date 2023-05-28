package com.ecloud.apps.watermeterreader.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ecloud.apps.watermeterreader.core.database.dao.BranchDao
import com.ecloud.apps.watermeterreader.core.database.dao.ProjectDao
import com.ecloud.apps.watermeterreader.core.database.model.BranchEntity
import com.ecloud.apps.watermeterreader.core.database.model.ConsumptionEntity
import com.ecloud.apps.watermeterreader.core.database.model.ProjectEntity

@Database(
    entities = [
        ProjectEntity::class,
        BranchEntity::class,
        ConsumptionEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class WmrDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun branchDao(): BranchDao
}
