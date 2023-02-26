package com.zhigaras.unsplash.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.unsplash.model.photoentity.PhotoEntity

@JsonClass(generateAdapter = true)
data class LikeResponseModel(
    @Json(name = "photo") val photo: PhotoEntity,
)