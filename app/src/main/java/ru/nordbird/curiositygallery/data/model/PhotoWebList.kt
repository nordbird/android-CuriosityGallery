package ru.nordbird.curiositygallery.data.model

import com.squareup.moshi.Json

data class PhotoWebList(
    @Json(name = "photos")
    val photos: List<PhotoWeb>
)
