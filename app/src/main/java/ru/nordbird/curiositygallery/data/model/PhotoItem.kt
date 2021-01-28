package ru.nordbird.curiositygallery.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "imgSrc")
    val imgSrc: String,

    @ColumnInfo(name = "earthDate")
    val earthDate: String,

    @ColumnInfo(name = "sol")
    val sol: Int,

    @ColumnInfo(name = "page")
    val page: Int,

    @ColumnInfo(name = "isBlocked")
    val isBlocked: Boolean
)
