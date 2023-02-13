package com.zhigaras.unsplash.model.photoentity


import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "id")
    val userId: String,
    @Json(name = "instagram_username")
    val instagramUsername: String?,
    @Json(name = "location")
    val userLocation: String?,
    @Json(name = "name")
    val fullName: String?,
    @Embedded
    @Json(name = "profile_image")
    val profileImage: ProfileImage,
    @Json(name = "username")
    val username: String?
)