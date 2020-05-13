package com.sample.android.cafebazaar.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for the category table.
 */
@Dao
interface CategoryDao {

    /**
     * Select all categories from the categories table.
     *
     * @return all categories.
     */
    @Query("SELECT * FROM databasecategory ORDER BY createdAt DESC")
    fun getCategories(): LiveData<List<DatabaseCategory>>

    /**
     * Insert a category in the database. If the category already exists, replace it.
     *
     * @param categories the categories to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg categories: DatabaseCategory)
}

@Database(entities = [DatabaseCategory::class], version = 1)
abstract class CategoryDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
}