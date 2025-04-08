package com.app.kaushalprajapati.musicguru.api.network

import com.app.kaushalprajapati.musicguru.api.service.YoutubeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {

    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService : YoutubeApiService by lazy {
        retrofit.create(YoutubeApiService::class.java)
    }
}