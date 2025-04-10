package com.app.kaushalprajapati.musicguru.ui.utils.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.kaushalprajapati.musicguru.R

class MyNotificationClass {

	@SuppressLint("ObsoleteSdkInt")
	fun showNotification(context: Context, title: String?, message: String?) {
		val channel_ID = "mediaPlayer_channel"
		val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channel_ID,
				"Welcome to MusicGuru🙏",
				NotificationManager.IMPORTANCE_DEFAULT
			)
			manager.createNotificationChannel(channel)
		}

		val notification = NotificationCompat.Builder(context, channel_ID)
			.setContentTitle(title)
			.setContentText(message)
			.setSmallIcon(R.drawable.imageapp)
			.build()

		manager.notify(1, notification)
	}
}