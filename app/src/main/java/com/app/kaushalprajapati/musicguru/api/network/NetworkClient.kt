package com.app.kaushalprajapati.musicguru.api.network

import com.app.kaushalprajapati.musicguru.api.service.YoutubeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
this is my api key url please use your
own url key from google console account
where you will found youtube V3 api
if you have any query or need help then
contact Email:📩 - kaushalprajapati9953@gmail.com
and found more creative android apps kotlin apps---
please visit my Github Profile🧑‍💻 - https://github.com/Itkaushal ::::
 */

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