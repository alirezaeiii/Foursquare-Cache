package com.sample.android.cafebazaar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.android.cafebazaar.domain.Category

@Entity
class DatabaseCategory(
    @PrimaryKey
    val id: String,
    val name: String,
    val pluralName: String,
    val shortName: String,
    val iconUrl: String,
    val venueId: String,
    val venueName: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: Long
)

fun List<DatabaseCategory>.asDomainModel(): List<Category> {
    return map {
        Category(
            id = it.id,
            name = it.name,
            pluralName = it.pluralName,
            shortName = it.shortName,
            iconUrl = it.iconUrl,
            venueId = it.venueId,
            venueName = it.venueName
        )
    }
}