package com.sample.android.cafebazaar.database

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DatabaseModule {

    @Module
    companion object {

        @Singleton
        @Provides
        @JvmStatic
        internal fun provideDb(context: Application) =
            Room.databaseBuilder(context.applicationContext, CategoryDatabase::class.java, "Categories.db")
                .build()

        @Singleton
        @Provides
        @JvmStatic
        internal fun provideCategoryDao(db: CategoryDatabase) = db.categoryDao
    }
}