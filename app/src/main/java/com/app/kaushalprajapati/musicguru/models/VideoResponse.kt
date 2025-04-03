package com.app.kaushalprajapati.musicguru.models


data class VideoResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics,
    val contentDetails: ContentDetails
)

data class VideoSnippet(
    val title: String,
    val description: String,
    val channelTitle: String,
    val thumbnails: Thumbnails,
    val channelId: String,
    val publishedAt: String
)

data class Thumbnails(
    val high: Thumbnail,
    val medium: Thumbnail
)
data class Thumbnail(
    val url: String
)

data class VideoStatistics(
    val viewCount: String,
    val likeCount: String
)

data class ContentDetails(val duration: String)

// Add these to your existing models

data class SearchResponse(
    val items: List<SearchItem>
)

data class SearchItem(
    val id: SearchId,
    val snippet: SearchSnippet
)

data class SearchId(
    val videoId: String
)

data class SearchSnippet(
    val title: String,
    val description: String,
    val channelTitle: String,
    val channelId: String,
    val publishedAt: String,
    val thumbnails: Thumbnails
)


