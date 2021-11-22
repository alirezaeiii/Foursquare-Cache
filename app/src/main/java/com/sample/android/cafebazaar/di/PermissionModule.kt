package com.sample.android.cafebazaar.di

import com.sample.android.cafebazaar.ui.MainFragment
import com.sample.android.cafebazaar.ui.PermissionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PermissionModule {

    @ContributesAndroidInjector
    internal abstract fun permissionFragment(): PermissionFragment
}