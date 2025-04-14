package com.app.kaushalprajapati.musicguru.ui.activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.app.kaushalprajapati.musicguru.databinding.ActivityVideoPlayerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request as OkHttpRequest
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.services.youtube.YoutubeService
import org.schabi.newpipe.extractor.MediaFormat
import org.schabi.newpipe.extractor.exceptions.ExtractionException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var youTubePlayerView: YouTubePlayerView
    private var videoId: String? = null
    private val TAG = "VideoPlayerActivity"

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        Log.d(TAG, "Permission result: isGranted=$isGranted")
        if (isGranted) {
            videoId?.let { downloadVideo(it) }
        } else {
            Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        videoId = intent.getStringExtra("VIDEO_ID")
        if (videoId == null) {
            Toast.makeText(this, "Invalid video ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        youTubePlayerView = binding.youtubePlayerView
        showLoading(true)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId!!, 0f)
                showLoading(false)
            }
        })

        binding.downloadButton?.setOnClickListener {
            Toast.makeText(this, "Initiating download...", Toast.LENGTH_SHORT).show()
            checkAndRequestPermission()
        }

        // Initialize NewPipe with custom Downloader
        NewPipe.init(OkHttpDownloader())
    }

    private fun checkAndRequestPermission() {
        Log.d(TAG, "Checking permissions for SDK ${Build.VERSION.SDK_INT}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d(TAG, "API >= 29, using scoped storage")
            videoId?.let { downloadVideo(it) }
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission already granted")
            videoId?.let { downloadVideo(it) }
        } else {
            Log.d(TAG, "Requesting WRITE_EXTERNAL_STORAGE permission")
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.loadingIndicator.root.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun downloadVideo(videoId: String) {
        showLoading(true)
        Log.d(TAG, "Starting video extraction for videoId=$videoId")
        lifecycleScope.launch {
            try {
                val youtubeUrl = "https://www.youtube.com/watch?v=$videoId"
                val streamInfo = withContext(Dispatchers.IO) {
                    // Try multiple clients with retries
                    val clients = listOf(
                        Pair(YoutubeService(0), "iOS client"),
                        Pair(
                            YoutubeService(0), "web client"
                        ),
                        Pair(
                            YoutubeService(0), "Android client"
                        )
                    )
                    var lastException: ExtractionException? = null
                    for ((client, clientName) in clients) {
                        repeat(2) { attempt ->
                            try {
                                Log.d(TAG, "Attempting extraction with $clientName, attempt ${attempt + 1}")
                                return@withContext StreamInfo.getInfo(client, youtubeUrl)
                            } catch (e: ExtractionException) {
                                Log.w(TAG, "$clientName extraction failed (attempt ${attempt + 1}): ${e.message}")
                                lastException = e
                                if (attempt == 0) delay(2000L) // Increased delay before retry
                            }
                        }
                    }
                    throw lastException ?: ExtractionException("All extraction attempts failed for videoId=$videoId")
                }
                Log.d(TAG, "Extraction successful, title=${streamInfo.name}")
                val videoStreams = streamInfo.videoStreams
                    .filter { it.format == MediaFormat.MPEG_4 }
                    .sortedBy { it.resolution.replace("p", "").toIntOrNull() ?: 0 }
                val downloadUrl = videoStreams.firstOrNull { it.resolution.contains("360") }?.url
                    ?: videoStreams.firstOrNull()?.url // Fallback to any MP4 stream
                if (downloadUrl != null) {
                    Log.d(TAG, "Download URL found: $downloadUrl")
                    val safeFileName = (streamInfo.name ?: "video_$videoId")
                        .replace("[^a-zA-Z0-9.-]".toRegex(), "_") + ".mp4"
                    val success = downloadFile(downloadUrl, safeFileName)
                    showLoading(false)
                    if (success) {
                        Toast.makeText(this@VideoPlayerActivity, "Video downloaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@VideoPlayerActivity, "Failed to download video", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e(TAG, "No suitable video stream found")
                    Toast.makeText(this@VideoPlayerActivity, "No suitable video format found", Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            } catch (e: ExtractionException) {
                Log.e(TAG, "Extraction error: ${e.message}", e)
                Toast.makeText(this@VideoPlayerActivity, "Unable to extract video: Video may be restricted or unavailable", Toast.LENGTH_LONG).show()
                showLoading(false)
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error: ${e.message}", e)
                Toast.makeText(this@VideoPlayerActivity, "Error downloading video: ${e.message}", Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        }
    }

    private suspend fun downloadFile(url: String, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting download for URL=$url, fileName=$fileName")
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
                val request = OkHttpRequest.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val file = File(getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS), fileName)
                    Log.d(TAG, "Saving file to: ${file.absolutePath}")
                    response.body?.byteStream()?.use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d(TAG, "Download completed successfully")
                    true
                } else {
                    Log.e(TAG, "Download failed: HTTP ${response.code}")
                    false
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}", e)
                false
            } catch (e: Exception) {
                Log.e(TAG, "Download error: ${e.message}", e)
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
    }

    // Custom Downloader implementation for NewPipe
    private class OkHttpDownloader : Downloader() {
        private val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        override fun execute(request: Request): Response {
            val okHttpRequestBuilder = OkHttpRequest.Builder()
                .url(request.url())
                .method(request.httpMethod(), if (request.httpMethod() == "POST" && request.dataToSend() != null) {
                    request.dataToSend()!!.toRequestBody()
                } else {
                    null
                })
                .also { builder ->
                    request.headers().forEach { (name, values) ->
                        values.forEach { value ->
                            builder.addHeader(name, value)
                        }
                    }
                }

            val okHttpRequest = okHttpRequestBuilder.build()
            val response = client.newCall(okHttpRequest).execute()
            val headers = mutableMapOf<String, MutableList<String>>()
            response.headers.forEach { (name, value) ->
                headers.computeIfAbsent(name) { mutableListOf() }.add(value)
            }

            return Response(
                response.code,
                response.message,
                headers,
                response.body?.string(),
                response.request.url.toString()
            )
        }
    }
}