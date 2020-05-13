package com.sample.android.cafebazaar.di

import androidx.lifecycle.ViewModelProvider
import com.sample.android.cafebazaar.R
import com.sample.android.cafebazaar.ui.DetailFragment
import com.sample.android.cafebazaar.ui.DetailFragmentArgs
import com.sample.android.cafebazaar.ui.MainActivity
import com.sample.android.cafebazaar.viewmodels.DetailViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailModule {

    @ContributesAndroidInjector
    internal abstract fun detailFragment(): DetailFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: DetailViewModel.Factory): ViewModelProvider.Factory

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideVenueId(activity: MainActivity): String {
            val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val fragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
            return DetailFragmentArgs.fromBundle(fragment?.requireArguments()!!).category.venueId
        }
    }
}