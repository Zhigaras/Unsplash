package com.zhigaras.unsplash.model.photodetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "name")
    val name: String?,
    @Json(name = "city")
    val city: String?,
    @Json(name = "country")
    val country:String?
)

@JsonClass(generateAdapter = true)
data class Position(
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "longitude")
    val longitude: Double?
)