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
        @Query("maxResults") maxResults: Int = 22,
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    @GET("search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 100,
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<SearchResponse>

    @GET("videos")
    suspend fun getTrendingVideos(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",  // ✅ Use mostPopular
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "17",  // ✅ Fetch all trending videos
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    // get movie
    @GET("videos")
    suspend fun getMovies(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",  // ✅ Use mostPopular
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "10",  // ✅ Use category ID for movies
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    @GET("videos")
    suspend fun getNews(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",  // ✅ Use mostPopular
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "25",  // ✅ Use category ID for news
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>

    @GET("videos")
    suspend fun getTechVideos(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",  // ✅ Use mostPopular
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "28",  // ✅ Use category ID for technology
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>


    @GET("videos")
    suspend fun getShortVideo(
        @Query("part") part: String = "snippet,statistics,contentDetails",
        @Query("chart") chart: String = "mostPopular",  // ✅ Use mostPopular
        @Query("regionCode") region: String = "IN",
        @Query("maxResults") maxResults: Int = 50,
        @Query("videoCategoryId") categoryId: String = "24",  // ✅ Use category ID for shorts
        @Query("key") apiKey: String = "AIzaSyDMcCQ-wX32yx9ttnBjf23cDlSE6cYPEb4"
    ): Response<VideoResponse>



}