package ru.nordbird.curiositygallery.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.nordbird.curiositygallery.data.model.PhotoDB
import ru.nordbird.curiositygallery.data.model.PhotoWebList
import ru.nordbird.curiositygallery.utils.PhotoResponse
import java.io.IOException


object WebService {
    private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos"
    private const val API_KEY = "UNbBjSQFTvdAI94NDhVKODALXlmn3xiKaAdTxZ73"

    private var client: OkHttpClient = OkHttpClient()

    suspend fun getPhotos(sol: Int, page: Int): PhotoResponse<List<PhotoDB>>? {
        val urlBuilder: HttpUrl.Builder = BASE_URL.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("sol", sol.toString())
        urlBuilder.addQueryParameter("page", page.toString())
        urlBuilder.addQueryParameter("api_key", API_KEY)

        val url = urlBuilder.build().toString()
        val request: Request = Request.Builder()
            .url(url)
            .build()


        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful && response.body != null) {
                        val counter = response.header("X-RateLimit-Remaining", "???") ?: "???"

                        val moshi = Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                        val adapter = moshi.adapter(PhotoWebList::class.java).nullSafe()

                        try {
                            adapter.fromJson(response.body!!.string())?.let { list ->
                                val photos = list.photos.map { it.toPhotoDB(page) }
                                PhotoResponse.success(
                                    "Loaded from Web (sol $sol, page $page) ($counter)",
                                    photos
                                )
                            }
                        } catch (e: IOException) {
                            PhotoResponse.error(
                                "Reading Data Error (sol $sol, page $page): ${e.message}",
                                emptyList()
                            )
                        }
                    } else {
                        PhotoResponse.error(
                            "Response Error (sol $sol, page $page)",
                            emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                PhotoResponse.error(
                    "Network Error (sol $sol, page $page): ${e.message}",
                    emptyList()
                )
            }
        }
    }
}
