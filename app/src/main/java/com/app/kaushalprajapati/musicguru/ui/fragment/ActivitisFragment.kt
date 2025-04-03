package com.app.kaushalprajapati.musicguru.ui.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.kaushalprajapati.musicguru.R
import com.app.kaushalprajapati.musicguru.databinding.FragmentActivitisBinding
import com.app.kaushalprajapati.musicguru.databinding.FragmentLoginBinding
import com.app.kaushalprajapati.musicguru.ui.activity.MainActivity
import com.app.kaushalprajapati.musicguru.ui.fragment.auth.LoginFragment
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import com.bumptech.glide.Glide

class ActivitisFragment : Fragment() {
	private lateinit var binding: FragmentActivitisBinding
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = FragmentActivitisBinding.inflate(inflater,container,false)!!

		return binding.root
	}

	@SuppressLint("SetTextI18n")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		if (prefsHelper.isLoggedIn(requireContext())) {
			val (savedName, _) = prefsHelper.getUser(requireContext())
			if (savedName != null) {
				binding.tvUserInfo.text = savedName.toString()
			} else {
				binding.tvUserInfo.text = "null"
			}
		} else{
			Toast.makeText(requireContext(), "you are not logged in?", Toast.LENGTH_SHORT).show()
			(activity as MainActivity).loadFragment(LoginFragment())
		}

		binding.btnLogOut.setOnClickListener{
			logOutUser()
			binding.tvUserInfo.text = "null"
		}

		binding.btnAboutUs.setOnClickListener {
			 aboutUsDialogOpen()
		}

		binding.btnAppVersion.setOnClickListener {
			appVersionDialogOpen()
		}

		binding.btnSearchHistory.setOnClickListener {
			// write code here for this .........
			showSearchHistoryDialog()
		}

		binding.btnAccountDetails.setOnClickListener {
			fetchUserDetails()
		}

		fetchuserImageView()

	}

	private fun fetchUserDetails() {
		showuserDetailsDialog()
	}

	private fun showuserDetailsDialog() {
		if (prefsHelper.isLoggedIn(requireContext())) {
			val user_detail = prefsHelper.getAllDetailsOfUser(this.requireContext())
			if (user_detail != null){
				val user_detail_text = """
				Name: ${user_detail.username ?: "N/A"  }
				Email: ${user_detail.email ?: "N/A"   }
				Gender: ${user_detail.gender ?: "N/A"  }
				Age: ${user_detail.age ?: "N/A"  }
				Password: ${user_detail.password ?: "N/A"  }
			""".trimIndent()

				// alert dialog
				AlertDialog.Builder(requireContext())
					.setTitle("Your Details")
					.setMessage(user_detail_text)
					.setPositiveButton("ok") { dialog, _ ->
						dialog.dismiss()
					}
					.create()
					.show()
			}else{
				Toast.makeText(requireContext(), "user not found?", Toast.LENGTH_SHORT).show()
			}
		} else{
			Toast.makeText(requireContext(), "user not logged in!", Toast.LENGTH_SHORT).show()
		}
	}

	@SuppressLint("ResourceAsColor")
	private fun fetchuserImageView() {

		val (savedName) = prefsHelper.getUser(requireContext())

			// No profile image, generate a placeholder with the first letter
			if (!savedName.isNullOrEmpty()) {
				val firstLetter = savedName[0].uppercaseChar().toString()
				val drawable = createCircularPlaceholder(firstLetter)
				binding.imageViewUser?.setImageDrawable(drawable)
			}
	}

	private fun createCircularPlaceholder(text: String): Drawable {
		val size = 100 // Size in pixels
		val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)

		val paint = Paint().apply {
			color = Color.GRAY
			isAntiAlias = true
		}
		val textPaint = Paint().apply {
			color = Color.WHITE
			textSize = 40f
			typeface = Typeface.DEFAULT_BOLD
			isAntiAlias = true
			textAlign = Paint.Align.CENTER
		}

		// Draw circle background
		canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
		// Draw text in the center
		val textBounds = Rect()
		textPaint.getTextBounds(text, 0, text.length, textBounds)
		val x = size / 2f
		val y = size / 2f - textBounds.exactCenterY()
		canvas.drawText(text, x, y, textPaint)

		return BitmapDrawable(resources, bitmap)
	}



	private fun showSearchHistoryDialog() {

		val historyKey = "search_history"
		val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("MusicGuruPrefs", 0)
		val searchHistory = sharedPreferences.getStringSet(historyKey, setOf())?.toList() ?: listOf()

		if (searchHistory.isEmpty()) {
			Toast.makeText(requireContext(), "No search history found!", Toast.LENGTH_SHORT).show()
			return
		}

		val builder = AlertDialog.Builder(requireContext())
		builder.setTitle("Search History")
		builder.setItems(searchHistory.toTypedArray()) { _, _ -> }
		builder.setPositiveButton("Clear History") { _, _ -> clearSearchHistory() }
		builder.setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
		builder.setIcon(R.drawable.searchvideo)
		builder.create().show()
	}

	private fun clearSearchHistory() {
		val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("MusicGuruPrefs", 0)
		sharedPreferences.edit().remove("search_history").apply()
		Toast.makeText(requireContext(), "Search history cleared!", Toast.LENGTH_SHORT).show()

	}

	private fun appVersionDialogOpen(){
		val builder = AlertDialog.Builder(requireContext())
		builder.setTitle("App Version")
		builder.setMessage("Music Guru and " +
				"currently running version is 1.0.0")
		builder.setPositiveButton("Ok") {dialog, _ -> dialog.dismiss()}
		builder.setIcon(R.drawable.appversion)
		builder.create().show()
	}

	private fun aboutUsDialogOpen() {
		val builder = AlertDialog.Builder(requireContext())
		builder.setTitle("About This App & us")
		builder.setMessage(
			"Music Guru is an advanced music streaming app that provides seamless playback, " +
					"high-quality video, and personalized recommendations. " +
			"Its Provide Youtube Music,Video,Movie,Drama,Comedy and so more." +
			"if you need any help or query ? then contact🌎 email @kaushalprajapati9953@gmail.com "+
			"and then we will help you."
		)
		builder.setPositiveButton("Ok") {dialog, _ -> dialog.dismiss()}
		builder.setIcon(R.drawable.about_icon)
		builder.create().show()

	}

	fun logOutUser(){
		prefsHelper.setLoggedIn(requireContext(),false)
		Toast.makeText(requireContext(), "LogOut Successfully!", Toast.LENGTH_SHORT).show()
		(activity as MainActivity).loadFragment(LoginFragment())
	}

}