package com.zhigaras.unsplash.model.photoitem


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "instagram_username")
    val instagramUsername: String?,
    @Json(name = "location")
    val location: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "profile_image")
    val profileImage: ProfileImage,
    @Json(name = "username")
    val username: String?
)