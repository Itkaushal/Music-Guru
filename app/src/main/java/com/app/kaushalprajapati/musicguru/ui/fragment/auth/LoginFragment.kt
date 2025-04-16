package com.app.kaushalprajapati.musicguru.ui.fragment.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.kaushalprajapati.musicguru.databinding.FragmentLoginBinding
import com.app.kaushalprajapati.musicguru.ui.activity.MainActivity
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginFragment : Fragment() {

	private lateinit var binding: FragmentLoginBinding
	private lateinit var googleSignInClient: GoogleSignInClient
	private lateinit var sharedPreferences: SharedPreferences

	private val RC_SIGN_IN = 100
	private val SHARED_PREF_NAME = "user_info"
	private val KEY_NAME = "name"
	private val KEY_EMAIL = "email"
	private val KEY_IMAGE = "image"

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentLoginBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

		// Configure Google Sign-In
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestEmail()
			.build()

		googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

		binding.googleloginButton?.setOnClickListener {
			val signInIntent = googleSignInClient.signInIntent
			startActivityForResult(signInIntent, RC_SIGN_IN)
		}

		binding.tvGoRegister.setOnClickListener {
			(activity as MainActivity).loadFragment(RegisterFragment())
		}
	}

	@Deprecated("Deprecated in Java")
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RC_SIGN_IN) {
			val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
			handleSignInResult(task)
		}
	}

	private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
		try {
			val account = completedTask.getResult(ApiException::class.java)

			val name = account?.displayName ?: "Unknown"
			val email = account?.email ?: "Unknown"
			val photoUrl = account?.photoUrl?.toString() ?: ""

			saveUserDetails(name, email, photoUrl)

			prefsHelper.setLoggedIn(requireContext(), true)

			Toast.makeText(requireContext(), "Welcome, $name!", Toast.LENGTH_SHORT).show()

			// Go to Main Fragment
			startActivity(Intent(requireContext(), MainActivity::class.java))
			requireActivity().finish()

		} catch (e: ApiException) {
			Log.w("LoginFragment", "Google sign in failed", e)
			Toast.makeText(requireContext(), "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
		}
	}

	private fun saveUserDetails(name: String, email: String, image: String) {
		sharedPreferences.edit().apply {
			putString(KEY_NAME, name)
			putString(KEY_EMAIL, email)
			putString(KEY_IMAGE, image)
			apply()
		}
	}
}
