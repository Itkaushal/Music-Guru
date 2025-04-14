package com.app.kaushalprajapati.musicguru.ui.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore.Video
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.kaushalprajapati.musicguru.R
import com.app.kaushalprajapati.musicguru.databinding.ItemVideoBinding
import com.app.kaushalprajapati.musicguru.models.VideoItem
import com.app.kaushalprajapati.musicguru.ui.activity.VideoPlayerActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoAdapter(
) : ListAdapter<VideoItem, VideoAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvTitle.text = item.snippet.title ?: "No Title"
            tvChannel.text = item.snippet.channelTitle ?: "Unknown Channel"
            tvViews.text = formatViews(item.statistics.viewCount ?:" No View Found")
            tvDuration.text = formatDuration(item.contentDetails.duration)

            Glide.with(holder.itemView.context)
                .load(item.snippet.thumbnails.high.url)
                .into(ivThumbnail)

            //click event to open VideoPlayerActivity
            root.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                    putExtra("VIDEO_ID", item.id)
                }
                context.startActivity(intent)
            }

        }
    }


    class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem) = oldItem == newItem
        }

        private fun formatViews(view: String?): String {
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
        }

        private fun formatDuration(duration: String?): String {
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
        }
    }
}
