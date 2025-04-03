package com.app.kaushalprajapati.musicguru.repository

import com.app.kaushalprajapati.musicguru.api.service.YoutubeApiService

class VideoRepository(private val apiService: YoutubeApiService) {

    suspend fun getMostPopularVideos() = apiService.getMostPopularVideos()

    suspend fun searchVideos(query: String) = apiService.searchVideos(query)

    suspend fun getTrendingVideos() = apiService.getTrendingVideos()

    suspend fun getMovies() = apiService.getMovies()

    suspend fun getNews() = apiService.getNews()

    suspend fun getTechVideos() = apiService.getTechVideos()

    suspend fun getShortVideo() = apiService.getShortVideo()

}