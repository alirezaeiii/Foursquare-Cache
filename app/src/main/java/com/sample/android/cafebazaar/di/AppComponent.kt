package com.sample.android.cafebazaar.di

import android.app.Application
import com.sample.android.cafebazaar.FoursquareApplication
import com.sample.android.cafebazaar.database.DatabaseModule
import com.sample.android.cafebazaar.network.Network
import com.sample.android.cafebazaar.service.ServiceBuilderModule

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Singleton
@Component(
    modules = [ActivityBindingModule::class,
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        BaseModule::class,
        Network::class,
        DatabaseModule::class,
        ServiceBuilderModule::class]
)
interface AppComponent : AndroidInjector<FoursquareApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}