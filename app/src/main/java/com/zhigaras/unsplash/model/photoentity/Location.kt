package com.zhigaras.unsplash.model.photoentity

import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "name")
    val locationName: String?,
    @Json(name = "city")
    val city: String?,
    @Json(name = "country")
    val country:String?,
    @Embedded
    @Json(name = "position")
    val position: Position
)

@JsonClass(generateAdapter = true)
data class Position(
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "longitude")
    val longitude: Double?
)