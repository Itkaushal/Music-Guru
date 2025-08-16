package com.app.kaushalprajapati.musicguru.api.service

import com.app.kaushalprajapati.musicguru.models.SearchResponse
import com.app.kaushalprajapati.musicguru.models.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {

    @GET("videos")
    suspend fun getMostPopularVideos(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    @GET("search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 50,
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<SearchResponse>

    @GET("videos")
    suspend fun getTrendingVideos(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "17",
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    // get movie
    @GET("videos")
    suspend fun getMovies(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "10",
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    @GET("videos")
    suspend fun getNews(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "25",
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    @GET("videos")
    suspend fun getTechVideos(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "28",
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>


    @GET("videos")
    suspend fun getShortVideo(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "24",
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

}