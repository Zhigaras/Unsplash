package com.zhigaras.unsplash.model.photoentity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLinks(
    @Json(name = "download")
    val userLinks_download: String,
    @Json(name = "download_location")
    val userLinks_downloadLocation: String,
    @Json(name = "html")
    val userLinks_html: String,
    @Json(name = "self")
    val userLinks_self: String,
    @Json(name = "likes")
    val userLinks_likes: String
)