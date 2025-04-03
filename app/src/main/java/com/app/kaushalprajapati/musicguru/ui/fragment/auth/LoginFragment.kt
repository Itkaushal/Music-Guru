package com.app.kaushalprajapati.musicguru.ui.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.kaushalprajapati.musicguru.databinding.FragmentLoginBinding
import com.app.kaushalprajapati.musicguru.ui.activity.MainActivity
import com.app.kaushalprajapati.musicguru.ui.fragment.HomeFragment
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import android.util.Log  // Add this for debugging

class LoginFragment : Fragment() {
	private lateinit var binding: FragmentLoginBinding

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

		binding.tvGoRegister.setOnClickListener {
			loadRegisterFragment()
		}

		binding.btnLogin.setOnClickListener {
			val name = binding.tvUserNameLogin.text.toString().trim()
			val password = binding.tvUserPasswordLogin.text.toString().trim()

			if (name.isBlank() || password.isBlank()) {
				Toast.makeText(requireContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			val userCredentials = prefsHelper.getUser(requireContext())

			// Debugging: Check if credentials are retrieved
			Log.d("LoginFragment", "Stored credentials: $userCredentials")

			if (userCredentials != null) {
				val (savedName, savedPassword) = userCredentials

				// Debugging: Log input vs stored values
				Log.d("LoginFragment", "Input: name=$name, password=$password")
				Log.d("LoginFragment", "Saved: name=$savedName, password=$savedPassword")

				if (name.equals(savedName) && password.equals(savedPassword)) {
					prefsHelper.setLoggedIn(requireContext(), true)
					Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()

					// Navigate to HomeFragment after successful login
					(activity as MainActivity).loadFragment(HomeFragment())
				} else {
					Toast.makeText(requireContext(), "Invalid credentials!", Toast.LENGTH_SHORT).show()
				}
			} else {
				Toast.makeText(requireContext(), "User not registered!", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun loadRegisterFragment() {
		(activity as MainActivity).loadFragment(RegisterFragment())
	}

}
