package ru.nordbird.curiositygallery.data.model

import java.util.*

data class PhotoItem(
    val id: Int = 0,
    val imgSrc: String = "",
    val earthDate: String = "",
    val isBlocked: Boolean = false
)
