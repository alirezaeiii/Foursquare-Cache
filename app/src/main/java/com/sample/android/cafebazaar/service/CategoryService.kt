package com.sample.android.cafebazaar.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 1000f, locationListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channelId"
            val channel = NotificationChannel(channelId, "Update location", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
            val notification: Notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("").setContentText("").build()
            startForeground(1, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
        compositeDisposable.clear()
    }
}