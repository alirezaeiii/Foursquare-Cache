package com.sample.android.cafebazaar.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sample.android.cafebazaar.BR
import com.sample.android.cafebazaar.R
import com.sample.android.cafebazaar.databinding.FragmentMainBinding
import com.sample.android.cafebazaar.util.isNetworkAvailable
import com.sample.android.cafebazaar.util.setupActionBar
import com.sample.android.cafebazaar.viewmodels.MainViewModel
import com.sample.android.cafebazaar.widget.MarginDecoration
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MainFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    /**
     * RecyclerView Adapter for converting a list of categories to cards.
     */
    private lateinit var viewModelAdapter: MainAdapter

    private var binding: FragmentMainBinding? = null

    private lateinit var viewModel: MainViewModel

    private var locationManager: LocationManager? = null

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (isNetworkAvailable(requireContext())) {
                viewModel.updateLocations(location)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (binding == null) {

            locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSIONS_REQUEST_LOCATION)
            } else {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 100f, locationListener)
            }

            viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

            binding = FragmentMainBinding.inflate(inflater, container, false).apply {
                setVariable(BR.vm, viewModel)
                // Set the lifecycleOwner so DataBinding can observe LiveData
                lifecycleOwner = viewLifecycleOwner
            }

            viewModelAdapter = MainAdapter(MainAdapter.OnClickListener { category ->
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailFragment(
                        category
                    )
                )
            })

            viewModel.category.observe(viewLifecycleOwner, Observer { categories ->
                viewModelAdapter.submitList(categories)
            })

            with(binding!!) {
                list.apply {
                    addItemDecoration(MarginDecoration(context))
                    setHasFixedSize(true)
                    adapter = viewModelAdapter
                }

                (activity as AppCompatActivity).setupActionBar(toolbar) {
                    setTitle(R.string.app_name)
                }
            }
        }

        return binding?.root
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission is granted

                    locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 100f, locationListener)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                                PERMISSIONS_REQUEST_LOCATION_IN_BACKGROUND)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.location_permission_not_granted, Toast.LENGTH_LONG).show()
                }
            PERMISSIONS_REQUEST_LOCATION_IN_BACKGROUND ->
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) { // Permission is not granted
                    Toast.makeText(requireContext(), R.string.location_permission_in_background_not_granted, Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(locationListener)
    }
}

private const val PERMISSIONS_REQUEST_LOCATION = 100
private const val PERMISSIONS_REQUEST_LOCATION_IN_BACKGROUND = 200