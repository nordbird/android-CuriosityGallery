package ru.nordbird.curiositygallery.utils

data class PhotoResponse<out T>(val status: Status, val data: T, val message: String?) {

    companion object {

        fun <T> success(msg: String, data: T): PhotoResponse<T> {
            return PhotoResponse(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String, data: T): PhotoResponse<T> {
            return PhotoResponse(Status.ERROR, data, msg)
        }

        fun <T> loading(msg: String, data: T): PhotoResponse<T> {
            return PhotoResponse(Status.LOADING, data, msg)
        }
    }

}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}