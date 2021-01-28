package ru.nordbird.curiositygallery.ui.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nordbird.curiositygallery.data.repository.PhotoRepository
import ru.nordbird.curiositygallery.utils.PhotoResponse

class GalleryViewModel : ViewModel() {
    private val photoRepository = PhotoRepository

    private val photoItems = Transformations.map(photoRepository.getPhotos()) { response ->
        val photos = response.data.filter { !it.isBlocked }
        return@map PhotoResponse(response.status, photos, response.message)
    }

    fun getPhotoData() = photoItems

    init {
        nextPage()
    }

    fun nextPage() {
        viewModelScope.launch {
            photoRepository.loadNewPhotos()
        }
    }


}