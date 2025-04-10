package com.app.kaushalprajapati.musicguru.ui.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.kaushalprajapati.musicguru.databinding.FragmentRegisterBinding
import com.app.kaushalprajapati.musicguru.ui.activity.MainActivity
import com.app.kaushalprajapati.musicguru.ui.utils.notification.MyNotificationClass
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper

class RegisterFragment : Fragment() {
	private lateinit var binding: FragmentRegisterBinding
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentRegisterBinding.inflate(inflater,container,false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.tvGoLogin.setOnClickListener {
			loadLoginFragment()
		}

		binding.btnRegister.setOnClickListener {

			val name = binding.tvUserName.text.toString().trim()
			val email = binding.tvUserEmail?.text.toString().trim()
			val gender = binding.tvUserGender?.text.toString().trim()
			val password = binding.tvUserPassword.text.toString().trim()
			val age = binding.tvUserAge?.text.toString().trim()

			if (name.isBlank() || password.isBlank() || email.isBlank() || gender.isBlank() || age.isBlank()) {
				Toast.makeText(requireContext(), "Fill in all credentials!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			else if (name.length < 3) {
				Toast.makeText(requireContext(), "Name must be at least 3 characters!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			else if (name.length > 12){
				Toast.makeText(requireContext(), "Name so long!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			else if (email.length < 6) {
				Toast.makeText(requireContext(), "Enter valid email!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			else if (gender.length < 4 || gender.length > 6) {
				Toast.makeText(requireContext(), "Gender must be between 4 and 6 characters!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			else if (age.toInt() < 18) {
				Toast.makeText(requireContext(), "Age must be at least 18+ ", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			else if (age.toInt() > 100) {
				Toast.makeText(requireContext(), "Age must be less than 100", Toast.LENGTH_SHORT).show()
				MyNotificationClass().showNotification(requireContext(),"Music Guru","Hello User I Think You need to pray God! not need to listen music😅")
				return@setOnClickListener
			}
			else if (password.length < 6) {
				Toast.makeText(requireContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			else if (password.length > 12){
				Toast.makeText(requireContext(), "Password must be between 6 and 12 characters!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			// Save user data using SharedPreferences
			prefsHelper.saveUser(requireContext(), name, password, email, gender, age )
			Toast.makeText(requireContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show()

			// Navigate to LoginFragment
			(activity as MainActivity).loadFragment(LoginFragment())
		}
	}

	private fun loadLoginFragment() {
		(activity as MainActivity).loadFragment(LoginFragment())
	}
}