package com.app.kaushalprajapati.musicguru.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.kaushalprajapati.musicguru.R
import com.app.kaushalprajapati.musicguru.ui.activity.ShortPlayerActivity
import com.app.kaushalprajapati.musicguru.databinding.ItemShortBinding
import com.app.kaushalprajapati.musicguru.models.VideoItem
import com.bumptech.glide.Glide

class ShortAdapter : ListAdapter<VideoItem, ShortAdapter.ViewHolder>(DIFF_CALLBACK) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding = ItemShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItem(position)
		with(holder.binding) {
			// Load thumbnail using Glide
			Glide.with(holder.itemView.context)
				.load(item.snippet.thumbnails.high.url)
				.placeholder(R.drawable.error_thumbnail)
				.error("https://cdn.iconscout.com/icon/free/png-512/free-error-icon-download-in-svg-png-gif-file-formats--page-not-found-bug-maintenance-customer-support-pack-people-icons-414985.png?f=webp&w=256")
				.into(ivThumbnail)

			// Handle click event to open VideoPlayerActivity
			root.setOnClickListener {
				val context = holder.itemView.context
				val intent = Intent(context, ShortPlayerActivity::class.java).apply {
					putExtra("VIDEO_ID", item.id) // Pass video ID
				}
				context.startActivity(intent)
			}

			// Handle channel name click to open channel page
			//tvChannel.setOnClickListener { onChannelClick?.invoke(item.snippet.channelId) }
		}
	}

	class ViewHolder(val binding: ItemShortBinding) : RecyclerView.ViewHolder(binding.root)

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoItem>() {
			override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem) = oldItem.id == newItem.id
			override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem) = oldItem == newItem
		}

		/*private fun formatViews(view: String?): String {
			return try {
				val viewCount = view?.toLongOrNull() ?: return "N/A views"
				when {
					viewCount >= 1_000_000 -> "${viewCount / 1_000_000}M views"
					viewCount >= 1_000 -> "${viewCount / 1_000}K views"
					else -> "$viewCount views"
				}
			} catch (e: Exception) {
				"N/A views"
			}
		}*/

		/*private fun formatDuration(duration: String?): String {
			if (duration.isNullOrEmpty()) return "00:00"
			val pattern = Regex("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
			val matchResult = pattern.matchEntire(duration)
			return matchResult?.let {
				val (hours, minutes, seconds) = it.destructured
				listOfNotNull(
					hours.takeIf { it.isNotEmpty() },
					minutes.padStart(2, '0'),
					seconds.padStart(2, '0')
				).joinToString(":")
			} ?: "00:00"
		}*/
	}

	/*var onChannelClick: ((String) -> Unit)? = null*/
}
