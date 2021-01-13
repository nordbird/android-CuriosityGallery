package ru.nordbird.curiositygallery.data.api

import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

object WebService {
    private val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos"
    private val API_KEY = "UNbBjSQFTvdAI94NDhVKODALXlmn3xiKaAdTxZ73"

    private var client: OkHttpClient = OkHttpClient()

    fun getPhotos(sol: Int, page: Int, responseCallback: Callback) {
        val urlBuilder: HttpUrl.Builder = BASE_URL.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("sol", sol.toString())
        urlBuilder.addQueryParameter("page", page.toString())
        urlBuilder.addQueryParameter("api_key", API_KEY)

        val url = urlBuilder.build().toString()
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(responseCallback)
    }
}
