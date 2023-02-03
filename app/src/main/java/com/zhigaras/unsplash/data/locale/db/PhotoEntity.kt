package com.zhigaras.unsplash.data.locale.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_photo")
data class PhotoEntity(
    val altDescription: String?,
    val description: String?,
    @PrimaryKey
    val id: String,
    val likedByUser: Boolean,
    val likes: Int,
    val downloadLink:String,
    val selfLink: String,
    val urlRegular: String,
    val urlRaw: String,
    val userUsername: String?,
    val userInstagramUsername: String?,
    val userProfileImage: String
)
