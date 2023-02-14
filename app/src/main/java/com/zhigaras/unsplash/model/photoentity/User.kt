package com.zhigaras.unsplash.model.photoentity


import androidx.room.Embedded
import androidx.room.Ignore
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User @JvmOverloads constructor(
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "id")
    val userId: String,
    @Ignore
    @Json(name = "instagram_username")
    val instagramUsername: String? = null,
    @Json(name = "location")
    val userLocation: String?,
    @Json(name = "name")
    val fullName: String?,
    @Embedded
    @Json(name = "profile_image")
    val profileImage: ProfileImage,
    @Json(name = "username")
    val username: String?,
    var instagramUsernameCorrect: String =
        instagramUsername?.let {
            if (it.contains('/'))
                it.split('/').last { it != "" }
            else it
        } ?: ""
)