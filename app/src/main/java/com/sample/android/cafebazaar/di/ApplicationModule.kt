package com.sample.android.cafebazaar.di

import android.app.Application
import android.content.Context
import com.sample.android.cafebazaar.util.schedulars.BaseSchedulerProvider
import com.sample.android.cafebazaar.util.schedulars.SchedulerProvider
import dagger.Binds
import dagger.Module


/**
 * This is a Dagger module. We use this to bind our Application class as a Context in the AppComponent
 * By using Dagger Android we do not need to pass our Application instance to any module,
 * we simply need to expose our Application as Context.
 * One of the advantages of Dagger.Android is that your
 * Application & Activities are provided into your graph for you.
 * [ ].
 */
@Module
abstract class ApplicationModule {
    //expose Application as an injectable context
    @Binds
    internal abstract fun bindContext(application: Application): Context

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}