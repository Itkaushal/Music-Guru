package com.app.kaushalprajapati.musicguru.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.kaushalprajapati.musicguru.databinding.ActivityShortPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ShortPlayerActivity : AppCompatActivity() {
	private lateinit var binding: ActivityShortPlayerBinding
	private lateinit var youTubePlayerView: YouTubePlayerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityShortPlayerBinding.inflate(layoutInflater)
		setContentView(binding.root)

		supportActionBar?.hide()

		val videoId = intent.getStringExtra("VIDEO_ID") ?: return

		youTubePlayerView = binding.youtubePlayerView

		showLoading(true)
		lifecycle.addObserver(youTubePlayerView)

		youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
			override fun onReady(youTubePlayer: YouTubePlayer) {
				youTubePlayer.loadVideo(videoId, 0f)
				showLoading(false)
			}
		})
	}

	private fun showLoading(loading: Boolean) {
		binding.loadingIndicator.root.visibility = if (loading) View.VISIBLE else View.GONE
	}
}