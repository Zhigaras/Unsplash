package com.zhigaras.unsplash.model.photoentity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.unsplash.domain.toSearchTagString

@Entity(tableName = "cached_photo")
@JsonClass(generateAdapter = true)
data class PhotoEntity @JvmOverloads constructor(
    @PrimaryKey
    @Json(name = "id") val id: String,
//    @Json(name = "current_user_collections")
//    val currentUserCollections: List<String>,
    @Json(name = "downloads") val downloads: Int?,
    @Json(name = "liked_by_user") val likedByUser: Boolean,
    @Json(name = "likes") val likes: Int,
    @Embedded
    @Json(name = "urls") val urls: Urls,
    @Embedded
    @Json(name = "exif") val exif: Exif?,
    @Embedded
    @Json(name = "location") val location: Location?,
    @Embedded
    @Json(name = "user") val user: User,
    @Embedded
    @Json(name = "links") val links: Links,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "description") val description: String?,
    @Json(name = "alt_description") val altDescription: String?,
    @Ignore
    @Json(name = "tags") val tags: List<Tag>? = null,
    var tagsString: String = tags?.toSearchTagString() ?: ""
)