package ru.nordbird.curiositygallery.data.model

data class PhotoItem(
    val id: Int,
    val imgSrc: String,
    val earthDate: String,
    val sol: Int,
    val page: Int,
    val isVisible: Boolean
)