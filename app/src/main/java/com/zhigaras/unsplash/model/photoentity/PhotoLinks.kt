package com.zhigaras.unsplash.model.photoentity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoLinks(
    @Json(name = "html") val html: String
)