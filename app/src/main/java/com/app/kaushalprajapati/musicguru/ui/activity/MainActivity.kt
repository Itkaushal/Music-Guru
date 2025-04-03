package com.app.kaushalprajapati.musicguru.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.kaushalprajapati.musicguru.R
import com.app.kaushalprajapati.musicguru.databinding.ActivityMainBinding
import com.app.kaushalprajapati.musicguru.ui.fragment.ActivitisFragment
import com.app.kaushalprajapati.musicguru.ui.fragment.HomeFragment
import com.app.kaushalprajapati.musicguru.ui.fragment.ShortsFragment
import com.app.kaushalprajapati.musicguru.ui.fragment.auth.LoginFragment
import com.app.kaushalprajapati.musicguru.ui.fragment.auth.RegisterFragment
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        bottomNavigationView = binding.bottomNavigation

        // Check if user is logged in
        if (prefsHelper.isLoggedIn(this)) {
            val (savedName, _) = prefsHelper.getUser(this)

            if (savedName != null) {
                Toast.makeText(this, "Welcome, $savedName!", Toast.LENGTH_SHORT).show()
            }
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
            super.onBackPressed() // Default behavior (closes activity)
        }
    }
}
