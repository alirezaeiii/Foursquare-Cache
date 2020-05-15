package com.sample.android.cafebazaar.viewmodels

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.cafebazaar.domain.Category
import com.sample.android.cafebazaar.repository.CategoryRepository
import javax.inject.Inject

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    private val repository: CategoryRepository
) : BaseViewModel() {

    private val _category = repository.categories
    val category: LiveData<List<Category>>
        get() = _category

    fun updateLocations(location: Location) {
        repository.refreshCategories(location.latitude, location.longitude)
            .also { compositeDisposable.add(it) }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
        private val repository: CategoryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}