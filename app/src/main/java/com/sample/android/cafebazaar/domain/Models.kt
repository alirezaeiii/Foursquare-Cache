package com.sample.android.cafebazaar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: String,
    val name: String,
    val pluralName: String,
    val shortName: String,
    val iconUrl: String,
    val venueId: String,
    val venueName: String
) : Parcelable

class Venue(
    val id: String,
    val name: String,
    val address: String,
    val likeCount: Int,
    val hours: String?,
    val photoUrl: String,
    val description: String?
)