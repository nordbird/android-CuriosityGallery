package ru.nordbird.curiositygallery.data.dao

import androidx.room.*
import ru.nordbird.curiositygallery.data.model.PhotoDB


@Database(entities = [PhotoDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos WHERE sol = :sol AND page = :page")
    suspend fun getPhotos(sol: Int, page: Int): List<PhotoDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoDB>)

    @Update
    suspend fun update(photo: PhotoDB)
}