package com.zhigaras.unsplash.model.photoitem


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.unsplash.data.locale.db.PhotoEntity

@JsonClass(generateAdapter = true)
data class PhotoItem(
    @Json(name = "alt_description")
    val altDescription: String?,
    @Json(name = "current_user_collections")
    val currentUserCollections: List<Any>,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "links")
    val links: Links,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "user")
    val user: User,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int
) {
    
    fun toPhotoEntity(): PhotoEntity {
        return PhotoEntity(
            altDescription = altDescription,
            description = description,
            id = id,
            likedByUser = likedByUser,
            likes = likes,
            downloadLink = links.download,
            selfLink = links.self,
            urlRegular = urls.regular,
            urlRaw = urls.raw,
            userUsername = user.name,
            userInstagramUsername = user.instagramUsername,
            userProfileImage = user.profileImage.medium,
            width = width,
            height = height
        )
    }
    
}