package com.zhigaras.unsplash.model.collectionentity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.unsplash.model.photoentity.Urls

@JsonClass(generateAdapter = true)
data class PreviewPhoto(
    @Json(name = "blur_hash")
    val blurHash: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "urls")
    val urls: Urls
)