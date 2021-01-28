package ru.nordbird.curiositygallery.data.dao

import androidx.room.Room
import ru.nordbird.curiositygallery.App

object PhotoDaoImpl {
    private var appDatabase: AppDatabase = Room.databaseBuilder(
        App.applicationContext(),
        AppDatabase::class.java, "photo_database"
    ).build()

    fun getDao(): PhotoDao = appDatabase.photoDao()
}