package com.ecloud.apps.watermeterreader.core.datastore.test

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.ecloud.apps.watermeterreader.core.datastore.UserPreferences
import com.ecloud.apps.watermeterreader.core.datastore.UserPreferencesSerializer
import com.ecloud.apps.watermeterreader.core.datastore.di.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        userPreferencesSerializer: UserPreferencesSerializer,
        tmpFolder: TemporaryFolder
    ): DataStore<UserPreferences> =
        tmpFolder.testUserPreferencesDataStore()
}
fun TemporaryFolder.testUserPreferencesDataStore(
    userPreferencesSerializer: UserPreferencesSerializer = UserPreferencesSerializer()
) = DataStoreFactory.create(
    serializer = userPreferencesSerializer,
) {
    newFile("user_preferences_test.pb")
}