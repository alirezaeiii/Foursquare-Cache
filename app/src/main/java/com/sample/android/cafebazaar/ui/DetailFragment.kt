package com.sample.android.cafebazaar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sample.android.cafebazaar.BR
import com.sample.android.cafebazaar.databinding.FragmentDetailBinding
import com.sample.android.cafebazaar.viewmodels.DetailViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DetailFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: DetailViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        val binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
        }

        with(binding) {
            toolbar.apply {
                setNavigationOnClickListener { findNavController().navigateUp() }
            }

            retryBtn.setOnClickListener {
                viewModel.showVenue()
            }
        }

        return binding.root
    }
}