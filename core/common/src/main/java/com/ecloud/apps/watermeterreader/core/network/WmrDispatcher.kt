package com.ecloud.apps.watermeterreader.core.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val wmrDispatcher: WmrDispatcher)

enum class WmrDispatcher {
    IO
}
