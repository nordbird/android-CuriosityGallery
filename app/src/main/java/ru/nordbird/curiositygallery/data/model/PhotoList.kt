package ru.nordbird.curiositygallery.data.model

import com.squareup.moshi.Json

data class PhotoList(
    @Json(name = "photos")
    val photos: List<Photo>
)
