package ru.nordbird.curiositygallery.data.model

import com.squareup.moshi.Json
import java.util.*

data class Photo(
    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "img_src")
    val imgSrc: String = "",

    @Json(name = "earth_date")
    val earthDate: String = ""
) {

    fun toPhotoItem(): PhotoItem {
        return PhotoItem(id, imgSrc, earthDate)
    }
}
