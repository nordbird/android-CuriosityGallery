package ru.nordbird.curiositygallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.nordbird.curiositygallery.data.model.PhotoItem
import ru.nordbird.curiositygallery.data.repository.PhotoRepository
import ru.nordbird.curiositygallery.extensions.mutableLiveData

class GalleryViewModel : ViewModel() {
    private val photoRepository = PhotoRepository
    private var sol = mutableLiveData(0)
    private var page = mutableLiveData(1)
    private var errorCount = 0

    private val photoItems =
        Transformations.map(photoRepository.loadPhotos(sol.value!!, page.value!!)) { photos ->
            return@map photos
                .filter { !it.isBlocked }
                .sortedBy { it.earthDate }
        }

    fun getPhotoData(): LiveData<List<PhotoItem>> {
        val result = MediatorLiveData<List<PhotoItem>>()

        val filterF = {
            val allPhotos = photoItems.value!!
            val newPhotos = photoRepository.loadPhotos(sol.value!!, page.value!!).value!!

            if (newPhotos.isEmpty()) {
                if (errorCount < 3) {
                    errorCount++
                    sol.value = sol.value!!.inc()
                }
            }else errorCount = 0


            val copy = allPhotos.toMutableList()
            copy.addAll(newPhotos.filter { !photoExists(it.id) })

            result.value = copy
                .filter { !it.isBlocked }
                .sortedBy { it.earthDate }
        }

        result.addSource(photoItems) { filterF.invoke() }
        result.addSource(sol) { filterF.invoke() }
        result.addSource(page) { filterF.invoke() }

        return result
    }

    fun photoExists(photoId: Int): Boolean = photoItems.value!!.find { it.id == photoId } != null

    fun nextPage() {
        page.value = page.value!!.inc()
    }


}