package com.ecloud.apps.watermeterreader.core.data.di

import com.ecloud.apps.watermeterreader.core.data.repository.AuthRepository
import com.ecloud.apps.watermeterreader.core.data.repository.BranchRepository
import com.ecloud.apps.watermeterreader.core.data.repository.NetworkRepository
import com.ecloud.apps.watermeterreader.core.data.repository.NetworkRepositoryImpl
import com.ecloud.apps.watermeterreader.core.data.repository.OfflineFirstAuthRepository
import com.ecloud.apps.watermeterreader.core.data.repository.OfflineFirstBranchRepository
import com.ecloud.apps.watermeterreader.core.data.repository.OfflineFirstUserDataRepository
import com.ecloud.apps.watermeterreader.core.data.repository.OfflineFirstProjectRepository
import com.ecloud.apps.watermeterreader.core.data.repository.UserDataRepository
import com.ecloud.apps.watermeterreader.core.data.repository.ProjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsAuthRepository(authRepository: OfflineFirstAuthRepository): AuthRepository

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository
    ): UserDataRepository

    @Binds
    fun bindsProjectRepository(
        projectRepository: OfflineFirstProjectRepository
    ): ProjectRepository

    @Binds
    fun bindsNetworkRepository(networkRepositoryImpl: NetworkRepositoryImpl): NetworkRepository

    @Binds
    fun bindsBranchRepository(branchRepository: OfflineFirstBranchRepository): BranchRepository

}
