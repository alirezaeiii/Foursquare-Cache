package com.sample.android.cafebazaar.service

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeFoursquareService(): FoursquareService
}