package com.sample.android.cafebazaar.di

import com.sample.android.cafebazaar.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
        modules = [MainModule::class,
            DetailModule::class]
    )
    internal abstract fun mainActivity(): MainActivity
}
