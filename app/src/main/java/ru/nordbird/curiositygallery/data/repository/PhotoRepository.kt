package ru.nordbird.curiositygallery.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nordbird.curiositygallery.data.api.WebService
import ru.nordbird.curiositygallery.data.dao.PhotoDaoImpl
import ru.nordbird.curiositygallery.data.model.PhotoItem
import ru.nordbird.curiositygallery.extensions.mutableLiveData
import ru.nordbird.curiositygallery.utils.PhotoResponse
import ru.nordbird.curiositygallery.utils.Status

object PhotoRepository {
    private val photoDB = PhotoDaoImpl.getDao()
    private val photoWeb = WebService

    private val photoItems = mutableLiveData(PhotoResponse.loading("", listOf<PhotoItem>()))
    private var sol = 0
    private var page = 0
    private var loadingError = false

    fun getPhotos() = photoItems

    suspend fun loadNewPhotos() {
        if (!loadingError) page++

        withContext(Dispatchers.IO) {
            var result = loadPhotosFromDB(sol, page)
            if (result.data.isNullOrEmpty()) result = loadPhotosFromWeb(sol, page)

            loadingError = result.status == Status.ERROR

            if (result.data.isNullOrEmpty() && !loadingError) {
                sol++
                page = 0
            }

            val allPhotos = photoItems.value!!.data.toMutableList()

            if (!result.data.isNullOrEmpty()) {
                val newPhotos = result.data
                allPhotos.addAll(newPhotos.filter { !photoExists(it.id) })
            }

            photoItems.postValue(PhotoResponse(result.status, allPhotos, result.message))
        }

    }

    fun find(photoId: Int): PhotoItem? = photoItems.value!!.data.find { it.id == photoId }

    suspend fun update(photo: PhotoItem) {
        val response = photoItems.value!!

        val copy = response.data.toMutableList()
        val ind = response.data.indexOfFirst { it.id == photo.id }
        if (ind == -1) return
        copy[ind] = photo

        photoDB.update(photo)

        photoItems.value = PhotoResponse(response.status, copy, response.message)
    }

    private suspend fun loadPhotosFromDB(sol: Int, page: Int): PhotoResponse<List<PhotoItem>> {
        return PhotoResponse.success(
            "Loaded from DB (sol $sol, page $page)",
            photoDB.getPhotos(sol, page)
        )
    }

    private suspend fun loadPhotosFromWeb(sol: Int, page: Int): PhotoResponse<List<PhotoItem>> {
        val result = photoWeb.getPhotos(sol, page) ?: PhotoResponse.error(
            "Empty Response",
            emptyList()
        )
        addPhotos(result.data)
        return result
    }

    private fun photoExists(photoId: Int): Boolean = find(photoId) != null

    private suspend fun addPhotos(photoList: List<PhotoItem>?) {
        if (photoList.isNullOrEmpty()) return

        photoDB.insertAll(photoList)
    }

}