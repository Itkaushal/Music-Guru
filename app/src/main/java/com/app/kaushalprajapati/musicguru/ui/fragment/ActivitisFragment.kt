package com.app.kaushalprajapati.musicguru.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.app.kaushalprajapati.musicguru.R
import com.app.kaushalprajapati.musicguru.databinding.FragmentActivitisBinding
import com.app.kaushalprajapati.musicguru.ui.activity.MainActivity
import com.app.kaushalprajapati.musicguru.ui.fragment.auth.LoginFragment
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class ActivitisFragment : Fragment() {

	private lateinit var binding: FragmentActivitisBinding
	private lateinit var googleSignInClient: GoogleSignInClient
	private var sharedPreferences: SharedPreferences? = null
	private lateinit var imageView: CircleImageView

	private val SHARED_PREF_NAME = "user_info"
	private val KEY_NAME = "name"
	private val KEY_EMAIL = "email"
	private val KEY_IMAGE = "image"

	private var userName: String? = null
	private var userEmail: String? = null
	private var userPhoto: String? = null

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentActivitisBinding.inflate(inflater, container, false)
		return binding.root
	}

	@SuppressLint("SetTextI18n")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		imageView = binding.imageViewUser as CircleImageView

		sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
		userName = sharedPreferences?.getString(KEY_NAME, null)
		userEmail = sharedPreferences?.getString(KEY_EMAIL, null)
		userPhoto = sharedPreferences?.getString(KEY_IMAGE, null)

		if (userName.isNullOrEmpty() || userEmail.isNullOrEmpty() || userPhoto.isNullOrEmpty()) {
			val intent = requireActivity().intent
			intent?.let {
				userName = it.getStringExtra("name")
				userEmail = it.getStringExtra("email")
				userPhoto = it.getStringExtra("image")
				saveUserDetails(userName, userEmail, userPhoto)
			}
		}

		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestEmail()
			.build()
		googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

		if (prefsHelper.isLoggedIn(requireContext())) {
			binding.tvUserInfo.text = userName ?: "Unknown"
		} else {
			Toast.makeText(requireContext(), "You're not logged in!", Toast.LENGTH_SHORT).show()
			(activity as MainActivity).loadFragment(LoginFragment())
		}

		fetchuserImageView()
		binding.btnLogOut.setOnClickListener { logOutUser() }
		binding.btnAboutUs.setOnClickListener { aboutUsDialogOpen() }
		binding.btnAppVersion.setOnClickListener { appVersionDialogOpen() }
		binding.btnSearchHistory.setOnClickListener { showSearchHistoryDialog() }
		binding.btnAccountDetails.setOnClickListener { fetchUserDetails() }
		binding.btnShareApk.setOnClickListener { shareApkwithFriends() }
		binding.instaButton.setOnClickListener { openUrlinBrowser("https://www.instagram.com/erkaushalprajapati") }
		binding.facebookButton.setOnClickListener { openUrlinBrowser("https://www.facebook.com/इंजी कौशल प्रजापति मझिगवां") }
		binding.gitHubButton.setOnClickListener { openUrlinBrowser("https://github.com/Itkaushal") }
		binding.moreOptionsBtn.setOnClickListener {
			Toast.makeText(requireContext(), "coming soon...", Toast.LENGTH_SHORT).show()
		}
	}

	private fun saveUserDetails(username: String?, useremail: String?, userimage: String?) {
		sharedPreferences?.edit()?.apply {
			putString(KEY_NAME, username)
			putString(KEY_EMAIL, useremail)
			putString(KEY_IMAGE, userimage)
			apply()
		}
	}

	private fun fetchuserImageView() {
		if (!userPhoto.isNullOrEmpty()) {
			Picasso.get().load(userPhoto).placeholder(R.drawable.user_icon_input).into(imageView)
		} else {
			binding.imageViewUser.setBackgroundResource(R.drawable.user_icon_input)
		}
	}

	private fun openUrlinBrowser(url: String) {
		try {
			val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
			startActivity(intent)
		} catch (e: Exception) {
			Toast.makeText(requireContext(), "Unable to open link: ${e.message}", Toast.LENGTH_SHORT).show()
		}
	}

	private fun shareApkwithFriends() {
		try {
			val apkInfo = requireContext().applicationContext.applicationInfo
			val apkFile = File(apkInfo.sourceDir)
			val apkUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", apkFile)

			val shareIntent = Intent(Intent.ACTION_SEND).apply {
				type = "application/vnd.android.package-archive"
				putExtra(Intent.EXTRA_STREAM, apkUri)
				addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
			}

			startActivity(Intent.createChooser(shareIntent, "Share APK via"))
		} catch (e: Exception) {
			Toast.makeText(requireContext(), "Apk share failed: ${e.message}", Toast.LENGTH_SHORT).show()
		}
	}

	private fun showSearchHistoryDialog() {
		val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("MusicGuruPrefs", Context.MODE_PRIVATE)
		val searchHistory = sharedPreferences.getStringSet("search_history", setOf())?.toList() ?: listOf()

		if (searchHistory.isEmpty()) {
			Toast.makeText(requireContext(), "No search history found!", Toast.LENGTH_SHORT).show()
			return
		}

		val historyMessage = searchHistory.joinToString("\n")
		AlertDialog.Builder(requireContext())
			.setTitle("Search History")
			.setMessage(historyMessage)
			.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
			.setNegativeButton("Clear") { dialog, _ ->
				sharedPreferences.edit().remove("search_history").apply()
				Toast.makeText(requireContext(), "Search history cleared.", Toast.LENGTH_SHORT).show()
				dialog.dismiss()
			}
			.show()
	}

	private fun fetchUserDetails() {
		val message = """
			Name: ${userName ?: "Unknown"}
			Email: ${userEmail ?: "Unknown"}
		""".trimIndent()

		AlertDialog.Builder(requireContext())
			.setTitle("Account Details")
			.setMessage(message)
			.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
			.show()
	}

	private fun aboutUsDialogOpen() {
		AlertDialog.Builder(requireContext())
			.setTitle("About Us")
			.setMessage("MusicGuru is a smart and powerful music discovery app designed by Kaushal Prajapati.")
			.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
			.show()
	}

	private fun appVersionDialogOpen() {
		AlertDialog.Builder(requireContext())
			.setTitle("App Version")
			.setMessage("Version: 1.0.0")
			.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
			.show()
	}

	private fun logOutUser() {
		prefsHelper.setLoggedIn(requireContext(), false)
		googleSignInClient.signOut()
		Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
		(activity as MainActivity).loadFragment(LoginFragment())
	}
}
