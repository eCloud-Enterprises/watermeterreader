package com.ecloud.apps.watermeterreader.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ecloud.apps.watermeterreader.core.database.model.BranchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BranchDao {

    @Query("SELECT * FROM branches")
    fun getBranchListStream(): Flow<List<BranchEntity>>

    /**
     * Inserts [branchEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreBranches(branchEntities: List<BranchEntity>): List<Long>

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun updateBranches(entities: List<BranchEntity>)

    @Transaction
    suspend fun upsertBranches(entities: List<BranchEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreBranches,
        updateMany = ::updateBranches
    )

    /**
     * Deletes all rows in the db
     */
    @Query("DELETE FROM branches")
    suspend fun deleteAll()
}
