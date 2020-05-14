package com.sample.android.cafebazaar.viewmodels

import androidx.lifecycle.ViewModel
import com.sample.android.cafebazaar.util.EspressoIdlingResource
import com.sample.android.cafebazaar.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel @JvmOverloads constructor(
    private val schedulerProvider: BaseSchedulerProvider? = null) :
    ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    protected fun <T> composeObservable(task: () -> Observable<T>): Observable<T> = task()
        .doOnSubscribe { EspressoIdlingResource.increment() } // App is busy until further notice
        .subscribeOn(schedulerProvider?.io())
        .observeOn(schedulerProvider?.ui())
        .doFinally {
            if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                EspressoIdlingResource.decrement() // Set app as idle.
            }
        }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all disposables;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}