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
			Glide.with(holder.itemView.context)
				.load(item.snippet.thumbnails.high.url)
				.into(ivThumbnail)

			// click event to open VideoPlayerActivity
			root.setOnClickListener {
				val context = holder.itemView.context
				val intent = Intent(context, ShortPlayerActivity::class.java).apply {
					putExtra("VIDEO_ID", item.id)
				}
				context.startActivity(intent)
			}

		}
	}

	class ViewHolder(val binding: ItemShortBinding) : RecyclerView.ViewHolder(binding.root)
	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoItem>() {
			override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem) = oldItem.id == newItem.id
			override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem) = oldItem == newItem
		}
	}

}
