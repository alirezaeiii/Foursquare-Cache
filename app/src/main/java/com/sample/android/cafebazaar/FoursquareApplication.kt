package com.sample.android.cafebazaar

import com.sample.android.cafebazaar.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class FoursquareApplication : DaggerApplication() {

    /**
     * onCreate is called before the first screen is shown to the user.
     */
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}