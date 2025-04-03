package com.app.kaushalprajapati.musicguru.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kaushalprajapati.musicguru.models.ContentDetails
import com.app.kaushalprajapati.musicguru.models.SearchItem
import com.app.kaushalprajapati.musicguru.models.VideoItem
import com.app.kaushalprajapati.musicguru.models.VideoSnippet
import com.app.kaushalprajapati.musicguru.models.VideoStatistics
import com.app.kaushalprajapati.musicguru.repository.VideoRepository
import com.app.kaushalprajapati.musicguru.ui.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: VideoRepository) : ViewModel() {

    private val _videos = MutableLiveData<Resource<List<VideoItem>>>()
    val videos: LiveData<Resource<List<VideoItem>>> = _videos

    private val _movie = MutableLiveData<Resource<List<VideoItem>>>()
    val movie : LiveData<Resource<List<VideoItem>>> = _movie

    private  val _news = MutableLiveData<Resource<List<VideoItem>>>()
    val news : LiveData<Resource<List<VideoItem>>> = _news

    private val _techVideos = MutableLiveData<Resource<List<VideoItem>>>()
    val techVideos : LiveData<Resource<List<VideoItem>>> = _techVideos

    private val _trendingVideos = MutableLiveData<Resource<List<VideoItem>>>()
    val trendingVideos: LiveData<Resource<List<VideoItem>>> = _trendingVideos

    private val _allVideos = MutableLiveData<Resource<List<VideoItem>>>()
    val allVideos : LiveData<Resource<List<VideoItem>>> = _allVideos

    private val _shortVideos = MutableLiveData<Resource<List<VideoItem>>>()
    val shortVideos : LiveData<Resource<List<VideoItem>>> = _shortVideos

    // function of ,load initial  data
        fun loadInitialData() {
            viewModelScope.launch {
                _videos.postValue(Resource.Loading())
                try {
                    val response = repository.getMostPopularVideos()
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("HomeViewModel", "API Success: ${response.body()}")
                        _videos.postValue(Resource.Success(response.body()!!.items))
                    } else {
                        Log.e("HomeViewModel", "API Failed: ${response.errorBody()?.string()}")
                        _videos.postValue(Resource.Error("Failed to load videos"))
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Exception: ${e.message}")
                    _videos.postValue(Resource.Error(e.message ?: "Unknown error"))
                }
            }
        }

    // function of search videos

    fun searchVideos(query: String) {
        viewModelScope.launch {
            _videos.postValue(Resource.Loading())
            try {
                val response = repository.searchVideos(query)
                if (response.isSuccessful) {
                    val items = response.body()?.items?.let { list ->
                        list.filterNotNull().map { it.toVideoItem() }
                    } ?: emptyList()

                    _videos.postValue(Resource.Success(items))
                } else {
                    _videos.postValue(Resource.Error("Search failed!: ${response.message()}"))
                }
            } catch (e: Exception) {
                _videos.postValue(Resource.Error(e.message ?: "Search error?"))
            }
        }
    }

    // function of search item video

    fun SearchItem.toVideoItem(): VideoItem {
        return VideoItem(
            id = this.id.videoId,
            snippet = VideoSnippet(
                title = this.snippet.title,
                description = this.snippet.description,
                channelTitle = this.snippet.channelTitle,
                thumbnails = this.snippet.thumbnails,
                channelId = this.snippet.channelId,
                publishedAt = this.snippet.publishedAt
            ),
            statistics = VideoStatistics(
                viewCount = "0",
                likeCount = "0"
            ),
            contentDetails = ContentDetails(duration = "PT0S")
        )
    }

    // function of movie video fetching

    fun fetchMovies(){
        viewModelScope.launch {
            _movie.postValue(Resource.Loading())
            try {
                val response = repository.getMovies()
                if (response.isSuccessful && response.body() != null){
                    _movie.postValue(Resource.Success(response.body() !!.items))
                } else {
                    _movie.postValue(Resource.Error(" Failed to load movies! "))
                }
            } catch (e: Exception){
                _movie.postValue(Resource.Error(e.message ?: " Unknown Error ? "))
            }
        }
    }

    // function of news video fetching

    fun fetchNews(){
        viewModelScope.launch {
            _news.postValue(Resource.Loading())
            try {
                val response = repository.getNews()
                if (response.isSuccessful && response.body() != null){
                    _news.postValue(Resource.Success(response.body() !!.items))
                } else {
                    _news.postValue(Resource.Error("Failed to load news!"))
                }
            } catch ( e : Exception){
                _news.postValue(Resource.Error(e.message ?: " Unknown Error? "))
            }
        }
    }

    // function of tech video fetching

    fun fetchTechVideos(){
        viewModelScope.launch {
            _techVideos.postValue(Resource.Loading())
            try {
                val response = repository.getTechVideos()
                if ( response.isSuccessful && response.body() != null ){
                    _techVideos.postValue(Resource.Success(response.body() !!.items))
                } else {
                    _techVideos.postValue(Resource.Error("Failed to load tech videos!"))
                }
            } catch ( e: Exception ){
                _techVideos.postValue(Resource.Error(e.message ?: " Unknown Error? "))
            }
        }
    }

    // function of trending video fetching

    fun loadTrendingVideos() {
        viewModelScope.launch {
            _trendingVideos.postValue(Resource.Loading())
            try {
                val response = repository.getTrendingVideos()
                if (response.isSuccessful && response.body()!=null){
                    _trendingVideos.postValue(Resource.Success(response.body()!!.items))
                } else{
                    _trendingVideos.postValue(Resource.Error("Failed to load trending videos!"))

                }
            } catch (e: Exception){
                _trendingVideos.postValue(Resource.Error(e.message ?: " Unknown Error?"))
            }
        }
    }

    // function of all video fetching

    fun loadAllVideos(){
        viewModelScope.launch {
            _allVideos.postValue(Resource.Loading())
            try{
                val response = repository.getMostPopularVideos()
                if (response.isSuccessful && response.body() != null){
                    _allVideos.postValue(Resource.Success(response.body() !!.items))
                } else {
                    _allVideos.postValue(Resource.Error("Failed to load all Videos"))
                }
            } catch (e: Exception){
                _allVideos.postValue(Resource.Error(e.message ?: "Unknown Error? "))
            }
        }
    }

    // get short videos...................
    fun getShortVideoData() {
        viewModelScope.launch {
            _shortVideos.postValue(Resource.Loading())
            try {
                val response = repository.getShortVideo()
                if (response.isSuccessful && response.body() != null) {
                    Log.d("HomeViewModel", "API Success: ${response.body()}")
                    _shortVideos.postValue(Resource.Success(response.body()!!.items))
                } else {
                    Log.e("HomeViewModel", "API Failed: ${response.errorBody()?.string()}")
                    _shortVideos.postValue(Resource.Error("Failed to load videos"))
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
                _shortVideos.postValue(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }
}
