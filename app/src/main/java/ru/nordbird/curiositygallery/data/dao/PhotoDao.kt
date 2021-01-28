package ru.nordbird.curiositygallery.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.nordbird.curiositygallery.data.model.PhotoItem


@Database(entities = [PhotoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos WHERE sol = :sol AND page = :page")
    suspend fun getPhotos(sol: Int, page: Int): List<PhotoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoItem>)
}