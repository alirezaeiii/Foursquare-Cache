package com.sample.android.cafebazaar.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import com.sample.android.cafebazaar.repository.CategoryRepository
import com.sample.android.cafebazaar.util.isNetworkAvailable
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CategoryService : DaggerService() {

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

    private lateinit var locationManager: LocationManager

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L,
            1000f,
            locationListener
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
        compositeDisposable.clear()
    }
}