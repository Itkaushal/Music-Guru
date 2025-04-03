package com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object prefsHelper {

	private const val PREFS_NAME  = "AuthPrefs"
	private const val KEY_LOGGED_IN = "isLoggedIn"
	private const val USER_NAME = "username"
	private const val PASSWORD = "password"
	private const val EMAIL = "email"
	private const val GENDER = "gender"
	private const val AGE = "0"

	private fun getPrefs(context: Context): SharedPreferences? {
		return context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
	}

	@SuppressLint("CommitPrefEdits")
	fun saveUser(context: Context, username: String, password: String, email: String, gender: String, age: String) {
		getPrefs(context)?.edit()?.apply {
			putString(USER_NAME,username)
			putString(PASSWORD,password)
			putString(EMAIL,email)
			putString(GENDER,gender)
			putString(AGE,age)
			apply()
		}
	}

	fun getUser(context: Context): Pair<String?, String?> {
		val prefs = getPrefs(context)
		return Pair(prefs?.getString(USER_NAME, null),
			prefs?.getString(PASSWORD, null)
		)
	}

	fun getAllDetailsOfUser(context: Context) : UserDetails? {
		val prefs = getPrefs(context)
		return if (prefs != null){
			UserDetails(
				prefs.getString(USER_NAME,"null"),
				prefs.getString(EMAIL,"null"),
				prefs.getString(GENDER,"null"),
				prefs.getString(AGE,"null"),
				prefs.getString(PASSWORD,"null")
			)
		}
		else{
			null
		}

	}

	fun setLoggedIn(context: Context, isLoggedIn: Boolean){
		getPrefs(context)?.edit()?.putBoolean(KEY_LOGGED_IN, isLoggedIn)?.apply()
	}

	fun isLoggedIn(context: Context) : Boolean{
		return getPrefs(context)!!.getBoolean(KEY_LOGGED_IN, false)
	}

}

data class UserDetails(
	val username: String?,
	val email: String?,
	val gender: String?,
	val age: String?,
	val password: String?
)
