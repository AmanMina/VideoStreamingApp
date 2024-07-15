package com.example.videostreamingapp


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("videos")
    suspend fun searchVideos(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): VideoResponse

    companion object {
        private const val BASE_URL = "https://pixabay.com/api/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

data class VideoResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<Video>
)

data class Video(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val duration: Int,
    val videos: VideoDetails,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val userId: Int,
    val user: String,
    val userImageURL: String
)

data class VideoDetails(
    val large: VideoSize,
    val medium: VideoSize,
    val small: VideoSize,
    val tiny: VideoSize
)

data class VideoSize(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String
)

