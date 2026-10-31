package com.dapascript.mever

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.dapascript.mever.BuildConfig.REVENUECAT_API_KEY
import com.dapascript.mever.core.common.util.BackgroundRemovalProcessor
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
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

        setupRevenueCat()

        CoroutineScope(IO).launch {
            backgroundRemovalProcessor.preload()
        }
    }

    private fun setupRevenueCat() {
        try {
            Purchases.logLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.INFO
            Purchases.configure(
                PurchasesConfiguration.Builder(
                    this,
                    REVENUECAT_API_KEY
                ).build()
            )
            Timber.d("RevenueCat configured successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to configure RevenueCat")
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(factory)
            .build()
    }
}