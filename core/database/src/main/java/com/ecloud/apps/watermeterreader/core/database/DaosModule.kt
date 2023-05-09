package com.ecloud.apps.watermeterreader.core.database

import com.ecloud.apps.watermeterreader.core.database.dao.BranchDao
import com.ecloud.apps.watermeterreader.core.database.dao.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesWarehouseDao(
        database: WmrDatabase
    ): ProjectDao = database.projectDao()

    @Provides
    fun providesBranchDao(
        database: WmrDatabase
    ): BranchDao = database.branchDao()
}
