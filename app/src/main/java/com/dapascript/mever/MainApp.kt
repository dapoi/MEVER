package com.dapascript.mever

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.dapascript.mever.core.common.util.BackgroundRemovalProcessor
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
open class MainApp : Application(), Configuration.Provider {

    @Inject
    lateinit var factory: HiltWorkerFactory

    @Inject
    lateinit var backgroundRemovalProcessor: BackgroundRemovalProcessor

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        CoroutineScope(IO).launch {
            backgroundRemovalProcessor.preload()
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(factory)
            .build()
    }
}