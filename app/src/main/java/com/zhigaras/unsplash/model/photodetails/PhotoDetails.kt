package com.zhigaras.unsplash.model.photodetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.unsplash.model.photoitem.Links
import com.zhigaras.unsplash.model.photoitem.Urls
import com.zhigaras.unsplash.model.photoitem.User

@JsonClass(generateAdapter = true)
data class PhotoDetails(
    @Json(name = "current_user_collections")
    val currentUserCollections: List<Any>,
    @Json(name = "downloads")
    val downloads: Int,
    @Json(name = "id")
    val id: String,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "exif")
    val exif: Exif,
    @Json(name = "location")
    val location: Location,
    @Json(name = "tags")
    val tags: List<Tag>,
    @Json(name = "user")
    val user: User,
    @Json(name = "links")
    val links: Links
)