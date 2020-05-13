package com.sample.android.cafebazaar.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.sample.android.cafebazaar.R
import com.sample.android.cafebazaar.service.FoursquareService
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)

        val intent = Intent(this, FoursquareService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, FoursquareService::class.java)
        stopService(intent)
    }
}
