package com.sample.android.cafebazaar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sample.android.cafebazaar.BR
import com.sample.android.cafebazaar.R
import com.sample.android.cafebazaar.databinding.FragmentMainBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (binding == null) {

            val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

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
}