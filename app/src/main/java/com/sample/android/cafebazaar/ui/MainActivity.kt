package com.sample.android.cafebazaar.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.sample.android.cafebazaar.R
import com.sample.android.cafebazaar.service.CategoryService
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(this, CategoryService::class.java)
            startForegroundService(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission is granted
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                                PERMISSIONS_REQUEST_LOCATION_IN_BACKGROUND
                            )
                        }
                    } else {
                        val intent = Intent(this, CategoryService::class.java)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent)
                        } else {
                            startService(intent)
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.location_permission_not_granted, Toast.LENGTH_LONG).show()
                }
            PERMISSIONS_REQUEST_LOCATION_IN_BACKGROUND ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission is granted
                    val intent = Intent(this, CategoryService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent)
                    }
                } else {
                    Toast.makeText(this, R.string.location_permission_in_background_not_granted, Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, CategoryService::class.java)
        stopService(intent)
    }

}

private const val PERMISSIONS_REQUEST_LOCATION = 100
private const val PERMISSIONS_REQUEST_LOCATION_IN_BACKGROUND = 200
