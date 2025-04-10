package com.app.kaushalprajapati.musicguru.ui.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.app.kaushalprajapati.musicguru.ui.utils.notification.MyNotificationClass

class NetworkChangeReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		if (isConnected(context)) {
			Log.d("Network", "Internet Available!")
			MyNotificationClass().showNotification(context!!,"Music Guru","Listen Music And Video without any ads or interruption!")
		} else{
			Log.d("Network","Network Not Available?")
			Toast.makeText(context, "Check internet connection?", Toast.LENGTH_SHORT).show()
		}
	}

	private fun isConnected(context: Context?): Boolean {
		val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val networkInfo = connectivityManager.activeNetworkInfo
		return networkInfo != null && networkInfo.isConnected
	}

}
