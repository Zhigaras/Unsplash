package com.zhigaras.unsplash.model.collectionentity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import com.zhigaras.unsplash.model.photoentity.Tag
import com.zhigaras.unsplash.model.photoentity.User

@JsonClass(generateAdapter = true)
data class CollectionEntity(
    @Json(name = "cover_photo")
    val coverPhoto: PhotoEntity,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val collectionId: String,
    @Json(name = "preview_photos")
    val previewPhotos: List<PreviewPhoto>,
    @Json(name = "tags")
    val tags: List<Tag>,
    @Json(name = "title")
    val title: String,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "user")
    val user: User
)