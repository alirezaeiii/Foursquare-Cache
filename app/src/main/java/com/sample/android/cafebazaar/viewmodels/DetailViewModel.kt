package com.sample.android.cafebazaar.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.cafebazaar.domain.Venue
import com.sample.android.cafebazaar.network.FoursquareService
import com.sample.android.cafebazaar.network.asDomainModel
import com.sample.android.cafebazaar.util.Resource
import com.sample.android.cafebazaar.util.schedulars.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * DetailViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class DetailViewModel(
    schedulerProvider: BaseSchedulerProvider,
    private val api: FoursquareService,
    private val venueId: String
) : BaseViewModel(schedulerProvider) {

    private val _liveData = MutableLiveData<Resource<Venue>>()
    val liveData: LiveData<Resource<Venue>>
        get() = _liveData

    private val _venue = MutableLiveData<Venue>()
    val venue: LiveData<Venue>
        get() = _venue

    init {
        showVenue()
    }

    fun showVenue() {
        _liveData.value = Resource.Loading()
        composeObservable { api.getVenue(venueId) }
            .subscribe({ responseNetworkWrapper ->
                val venue = responseNetworkWrapper.response.venue.asDomainModel()
                _liveData.postValue(Resource.Success(venue))
                _venue.postValue(venue)
            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            }.also { compositeDisposable.add(it) }
    }

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory @Inject constructor(
        private val schedulerProvider: BaseSchedulerProvider,
        private val api: FoursquareService,
        private val venueId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(schedulerProvider, api, venueId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}