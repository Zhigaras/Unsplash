package com.zhigaras.unsplash.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.unsplash.model.photoentity.PhotoEntity

@JsonClass(generateAdapter = true)
data class SearchResponseModel(
    @Json(name = "total") val total: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "results") val results: List<PhotoEntity>,
)