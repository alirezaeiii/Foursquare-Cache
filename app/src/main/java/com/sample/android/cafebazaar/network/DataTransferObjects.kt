package com.sample.android.cafebazaar.network

import com.sample.android.cafebazaar.database.DatabaseCategory
import com.sample.android.cafebazaar.domain.Venue

class ResponseWrapper(val response: Response)

class Response(val groups: List<Group>)

class Group(val items: List<Item>)

class Item(val venue: VenueNetwork)

class VenueNetwork(
    val id: String,
    val name: String,
    val categories: List<CategoryNetwork>
)

class CategoryNetwork(
    val id: String,
    val name: String,
    val pluralName: String,
    val shortName: String,
    val icon: IconNetwork
)


class IconNetwork(
    val prefix: String,
    val suffix: String
)

fun Group.asDatabaseModel(latitude: Double, longitude: Double): Array<DatabaseCategory> {
    val databaseCategories = ArrayList<DatabaseCategory>()
    for (i in items.indices) {
        val item = items[i]
        for (j in item.venue.categories.indices) {
            val category = item.venue.categories[j]
            databaseCategories.add(
                DatabaseCategory(
                    id = category.id,
                    name = category.name,
                    pluralName = category.pluralName,
                    shortName = category.shortName,
                    iconUrl = category.icon.prefix + "88" + category.icon.suffix,
                    venueId = item.venue.id,
                    venueName = item.venue.name,
                    latitude = latitude,
                    longitude = longitude,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
    return databaseCategories.toTypedArray()
}

class ResponseNetworkWrapper(val response: ResponseNetwork)

class ResponseNetwork(val venue: VenueWrapper)

class VenueWrapper(
    val id: String,
    val name: String,
    val location: Location,
    val likes: Likes,
    val hours: Hours?,
    val bestPhoto: BestPhoto,
    val description: String?
)

class Location(
    val address: String?,
    val crossStreet: String?,
    val cc: String,
    val city: String?,
    val state: String?,
    val country: String
)

class Likes(val count: Int)

class Hours(val status: String?)

class BestPhoto(
    val id: String,
    val prefix: String,
    val suffix: String
)

fun VenueWrapper.asDomainModel() =
    Venue(
        id = id,
        name = name,
        address = (location.address ?: "") + " " +
                (location.crossStreet ?: "") + " " +
                (location.city ?: "") + " " +
                (location.state ?: "") + " " +
                location.cc + " " +
                location.country,
        likeCount = likes.count,
        hours = hours?.status,
        photoUrl = bestPhoto.prefix + "456" + bestPhoto.suffix,
        description = description
    )