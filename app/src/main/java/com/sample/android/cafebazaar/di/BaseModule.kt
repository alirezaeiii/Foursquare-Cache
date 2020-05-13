package com.sample.android.cafebazaar.di

import com.sample.android.cafebazaar.util.schedulars.BaseSchedulerProvider
import com.sample.android.cafebazaar.util.schedulars.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class BaseModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}