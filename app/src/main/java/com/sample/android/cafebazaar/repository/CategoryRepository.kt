package com.sample.android.cafebazaar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.sample.android.cafebazaar.database.CategoryDatabase
import com.sample.android.cafebazaar.database.asDomainModel
import com.sample.android.cafebazaar.domain.Category
import com.sample.android.cafebazaar.network.FoursquareService
import com.sample.android.cafebazaar.network.asDatabaseModel
import com.sample.android.cafebazaar.util.schedulars.BaseSchedulerProvider
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val database: CategoryDatabase,
    private val api: FoursquareService,
    private val schedulerProvider: BaseSchedulerProvider
) {

    /**
     * A list of categories that can be shown on the screen.
     */
    val categories: LiveData<List<Category>> =
        Transformations.map(database.categoryDao.getCategories()) {
            it.asDomainModel()
    }

    /**
     * Refresh the categories stored in the offline cache.
     */
    fun refreshCategories(latitude: Double, longitude: Double): Disposable =
        api.getCategories("$latitude,$longitude")
            .subscribeOn(schedulerProvider.io())
            .subscribe { responseWrapper ->
                database.categoryDao.insertAll(
                    *responseWrapper.response.groups[0].asDatabaseModel(
                        latitude,
                        longitude
                    )
                )
            }
}