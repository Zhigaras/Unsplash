package com.zhigaras.unsplash.model.photoentity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "likes")
    val user_likes: String
)