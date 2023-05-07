package com.ecloud.apps.watermeterreader.core.network

import com.ecloud.apps.watermeterreader.core.network.fake.FakeWmrNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {

    @Binds
    fun FakeWmrNetworkDataSource.binds(): WmrNetworkDataSource
}
