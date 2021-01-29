package ru.nordbird.curiositygallery.data.model

import com.squareup.moshi.Json

data class PhotoWeb(
    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "img_src")
    val imgSrc: String = "",

    @Json(name = "earth_date")
    val earthDate: String = "",

    @Json(name = "sol")
    val sol: Int = 0,

) {

    fun toPhotoDB(page: Int): PhotoDB {
        return PhotoDB(id, imgSrc, earthDate, sol, page, true)
    }
}
