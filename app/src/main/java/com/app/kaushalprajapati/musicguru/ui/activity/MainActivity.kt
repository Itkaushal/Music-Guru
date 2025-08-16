package com.app.kaushalprajapati.musicguru.ui.activity

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.kaushalprajapati.musicguru.R
import com.app.kaushalprajapati.musicguru.databinding.ActivityMainBinding
import com.app.kaushalprajapati.musicguru.ui.fragment.ActivitisFragment
import com.app.kaushalprajapati.musicguru.ui.fragment.HomeFragment
import com.app.kaushalprajapati.musicguru.ui.fragment.ShortsFragment
import com.app.kaushalprajapati.musicguru.ui.fragment.auth.LoginFragment
import com.app.kaushalprajapati.musicguru.ui.utils.notification.MyNotificationClass
import com.app.kaushalprajapati.musicguru.ui.utils.receiver.NetworkChangeReceiver
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var networkChangeReceiver: BroadcastReceiver
    private  val requestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requetsNotificationPermission()
        MyNotificationClass().showNotification(this,"Hi👋","Welcome to MusicGuru🎵")
        networkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        supportActionBar?.hide()
        bottomNavigationView = binding.bottomNavigation

        if (prefsHelper.isLoggedIn(this)) {
            val (savedName, _) = prefsHelper.getUser(this)
           /* if (savedName != null) {
                Toast.makeText(this, "Welcome, $savedName!", Toast.LENGTH_SHORT).show()
            }*/
            loadFragment(HomeFragment())  // Open Home if logged in
        } else {
            loadFragment(LoginFragment()) // Open Login if not logged in
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_shorts -> loadFragment(ShortsFragment())
                R.id.nav_activity -> loadFragment(ActivitisFragment())
                else -> false
            }
        }
    }

    fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
        return true
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


    private fun requetsNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MyNotificationClass().showNotification(this, "Hi", "Welcome to MusicGuru!")
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
