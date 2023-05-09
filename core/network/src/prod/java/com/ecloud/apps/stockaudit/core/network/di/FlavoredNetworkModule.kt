package com.ecloud.apps.stockaudit.core.network.di

import com.ecloud.apps.stockaudit.core.network.EbtNetworkDataSource
import com.ecloud.apps.stockaudit.core.network.retrofit.RetrofitEbtNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {

    @Binds
    fun RetrofitEbtNetwork.binds(): EbtNetworkDataSource
}
