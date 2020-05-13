package com.sample.android.cafebazaar.service

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.IBinder
import com.sample.android.cafebazaar.repository.CategoryRepository
import com.sample.android.cafebazaar.util.isNetworkAvailable
import com.sample.android.cafebazaar.util.registerLocationListener
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FoursquareService : DaggerService() {

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: CategoryRepository

    private val context = this

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

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        registerLocationListener(context, locationListener, 1000f)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}