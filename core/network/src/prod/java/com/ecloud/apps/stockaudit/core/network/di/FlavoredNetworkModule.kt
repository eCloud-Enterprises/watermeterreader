package com.ecloud.apps.stockaudit.core.network.di

import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.retrofit.RetrofitWmrNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {

    @Binds
    fun RetrofitWmrNetwork.binds(): WmrNetworkDataSource
}
