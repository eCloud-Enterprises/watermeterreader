package com.ecloud.apps.watermeterreader.core.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val ebtScannerDispatcher: WmrDispatcher)

enum class WmrDispatcher {
    IO
}
