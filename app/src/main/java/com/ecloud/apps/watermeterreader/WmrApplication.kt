package com.ecloud.apps.watermeterreader

import android.app.Application
import com.ecloud.apps.watermeterreader.sync.initializers.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WmrApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Sync.initialize(this)
    }
}