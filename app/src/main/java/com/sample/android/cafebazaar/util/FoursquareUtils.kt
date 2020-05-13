package com.sample.android.cafebazaar.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun registerLocationListener(context: Context, locationListener: LocationListener, minDistance: Float) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    try {
        // Request location updates
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L,
            minDistance,
            locationListener
        )
    } catch (err: SecurityException) {
        Timber.e("Security Exception, no location available")
    }
}

@Suppress("DEPRECATION")
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

@SuppressLint("SimpleDateFormat")
fun toSimpleString(date: Date) : String {
    val format = SimpleDateFormat("yyyyMMdd")
    return format.format(date)
}

