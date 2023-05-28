package com.ecloud.apps.watermeterreader.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ecloud.apps.watermeterreader.core.database.model.ConsumptionEntity
import com.ecloud.apps.watermeterreader.core.database.model.ProjectEntity
import com.ecloud.apps.watermeterreader.core.database.model.ProjectWithConsumptionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects")
    fun getProjectsStream(): Flow<List<ProjectEntity>>

    /**
     * Gets the list of [ProjectEntity] with the list of [ConsumptionEntity]
     */
    @Transaction
    @Query("SELECT * FROM projects")
    fun getProjectsWithConsumptionsStream(): Flow<List<ProjectWithConsumptionsEntity>>

    @Update
    fun updateConsumption(consumptionEntity: ConsumptionEntity)

    /**
     * Inserts [consumptionEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreConsumptions(consumptionEntities: List<ConsumptionEntity>): List<Long>

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they dont'
     */
    @Update
    suspend fun updateConsumptions(entities: List<ConsumptionEntity>)

    @Transaction
    suspend fun upsertConsumptions(entities: List<ConsumptionEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreConsumptions,
        updateMany = ::updateConsumptions,
    )

    @Query("DELETE FROM projects")
    suspend fun deleteAllProjects()

    /**
     * Inserts [warehouseEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreProjects(warehouseEntities: List<ProjectEntity>): List<Long>

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they dont'
     */
    @Update
    suspend fun updateProjects(entities: List<ProjectEntity>)

    @Transaction
    suspend fun upsertProjects(entities: List<ProjectEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreProjects,
        updateMany = ::updateProjects
    )

    /**
     * Deletes rows in the db matching the specified [codes]
     */
    @Query(
        value = """
           DELETE FROM projects
           WHERE code in (:codes)
       """
    )
    suspend fun deleteProjects(codes: List<String>)
}
