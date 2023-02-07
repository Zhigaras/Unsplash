package com.zhigaras.unsplash.presentation

import com.zhigaras.unsplash.data.locale.db.PhotoEntity

object TestPhotoModel {
    
    val testPhotoModel = PhotoEntity(
        altDescription = "altDesc",
        description = "desc",
        downloadLink = "",
        id = "asdasd",
        likes = 123,
        likedByUser = false,
        selfLink = "",
        urlRaw = "https://images.unsplash.com/photo-1661956603025-8310b2e3036d?ixid=Mnw0MDQxMjd8MXwxfGFsbHwxfHx8fHx8Mnx8MTY3NTMwNjY5NA&ixlib=rb-4.0.3",
        urlRegular = "https://images.unsplash.com/photo-1661956603025-8310b2e3036d?crop=entropy&cs=tinysrgb&fm=jpg&ixid=Mnw0MDQxMjd8MXwxfGFsbHwxfHx8fHx8Mnx8MTY3NTMwNjY5NA&ixlib=rb-4.0.3&q=80",
        userInstagramUsername = "userInstagram",
        userProfileImage = "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
        userUsername = "userName",
        width = 1000,
        height = 700
    )
}