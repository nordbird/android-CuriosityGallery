package ru.nordbird.curiositygallery.data.repository

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Call
import okhttp3.Response
import ru.nordbird.curiositygallery.data.api.WebService
import ru.nordbird.curiositygallery.data.model.PhotoItem
import ru.nordbird.curiositygallery.data.model.PhotoList
import ru.nordbird.curiositygallery.extensions.mutableLiveData
import java.io.IOException
import javax.security.auth.callback.Callback

object PhotoRepository {

    private val photos: MutableLiveData<List<PhotoItem>> = mutableLiveData(listOf())

    fun loadPhotos(sol: Int, page: Int): MutableLiveData<List<PhotoItem>> {
        WebService.getPhotos(sol, page, object : Callback, okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                addPhotos(listOf())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && response.body != null) {
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                    val adapter = moshi.adapter(PhotoList::class.java).nullSafe()

                    try {
                        adapter.fromJson(response.body!!.string())?.let { list ->
                            addPhotos(list.photos.map { it.toPhotoItem() })
                        }
                    } catch (e: IOException) {
                        addPhotos(listOf())
                    }
                } else {
                    addPhotos(listOf())
                }
            }
        })

        return photos
    }


    fun photoExists(photoId: Int): Boolean = photos.value!!.find { it.id == photoId } != null

    fun addPhotos(photoList: List<PhotoItem>) {
        if (photoList.isEmpty()) return
        
        val copy = photos.value!!.toMutableList()
        copy.addAll(photoList.filter { !photoExists(it.id) })
        photos.postValue(copy)
    }

}