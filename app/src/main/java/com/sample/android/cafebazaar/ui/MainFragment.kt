package com.sample.android.cafebazaar.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private lateinit var locationManager: LocationManager

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationManager()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
        }

        val viewModelAdapter = MainAdapter(MainAdapter.OnClickListener { category ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(
                    category
                )
            )
        })

        viewModel.category.observe(viewLifecycleOwner, { categories ->
            viewModelAdapter.submitList(categories)
        })

        with(binding) {
            list.apply {
                addItemDecoration(MarginDecoration(context))
                setHasFixedSize(true)
                adapter = viewModelAdapter
            }

            (activity as AppCompatActivity).setupActionBar(toolbar) {
                setTitle(R.string.app_name)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    @SuppressLint("MissingPermission")
    private fun initLocationManager() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L,
            100f,
            locationListener
        )
    }
}