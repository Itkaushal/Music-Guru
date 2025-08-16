package com.app.kaushalprajapati.musicguru.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.app.kaushalprajapati.musicguru.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var youTubePlayerView: YouTubePlayerView
    private var videoId: String? = null


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

    }

    private fun showLoading(loading: Boolean) {
        binding.loadingIndicator.root.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
    }

}