package com.zhigaras.unsplash.model.photodetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "type")
    val type: String,
    @Json(name = "title")
    val title: String
)