package com.ecloud.apps.watermeterreader.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ecloud.apps.watermeterreader.core.database.model.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects")
    fun getProjectsStream(): Flow<List<ProjectEntity>>

    @Insert
    suspend fun insertAll(entities: List<ProjectEntity>)

    @Query("DELETE FROM projects")
    suspend fun deleteAll()

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
