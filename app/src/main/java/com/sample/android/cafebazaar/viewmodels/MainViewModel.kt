package com.sample.android.cafebazaar.viewmodels

import android.app.Application
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.cafebazaar.domain.Category
import com.sample.android.cafebazaar.repository.CategoryRepository
import com.sample.android.cafebazaar.util.isNetworkAvailable
import com.sample.android.cafebazaar.util.registerLocationListener
import com.sample.android.cafebazaar.util.schedulars.BaseSchedulerProvider
import javax.inject.Inject

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    schedulerProvider: BaseSchedulerProvider,
    private val repository: CategoryRepository,
    app: Application
) : BaseViewModel(schedulerProvider) {

    private val context = app

    private val _category = repository.categories
    val category: LiveData<List<Category>>
        get() = _category

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (isNetworkAvailable(context)) {
                repository.refreshCategories(location.latitude, location.longitude)
                    .also { compositeDisposable.add(it) }
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    init {
        registerLocationListener(context, locationListener, 100f)
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
        private val schedulerProvider: BaseSchedulerProvider,
        private val repository: CategoryRepository,
        private val app: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(schedulerProvider, repository, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}