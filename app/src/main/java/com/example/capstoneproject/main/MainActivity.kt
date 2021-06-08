package com.example.capstoneproject.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityMainBinding
import com.example.capstoneproject.main.dashboard.DashboardFragment
import com.example.capstoneproject.main.news.NewsFragment
import com.example.capstoneproject.main.profile.ProfileViewFragment
import com.example.capstoneproject.viewmodel.UserViewModel
import com.example.core.domain.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            val user = bundle.getParcelable<User>(USER_SIGN_IN)
            if (user?.userId != null) {
                checkSignIn(user)
            }
        }

        setFirstFragment()
        setSelectedFragment()
    }

    private fun setFirstFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, NewsFragment()).commit()
    }

    private fun setSelectedFragment() {
        binding.navView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemReselectedListener,
                BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemReselected(item: MenuItem) {
                when (item.itemId) {
                    R.id.navigation_notifications -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, NewsFragment()).addToBackStack(null).commit()
                    R.id.navigation_dashboard -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, DashboardFragment()).addToBackStack(null).commit()
                    R.id.navigation_profile -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, ProfileViewFragment()).addToBackStack(null).commit()
                }
            }

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_notifications -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, NewsFragment()).addToBackStack(null).commit()
                    R.id.navigation_dashboard -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, DashboardFragment()).addToBackStack(null).commit()
                    R.id.navigation_profile -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, ProfileViewFragment()).addToBackStack(null).commit()
                }
                return true
            }
        })
    }

    private fun checkSignIn(user: User) {
        userViewModel.setUser(user)
        userViewModel.setIsLoggedIn(true)
        lifecycleScope.launch {
            userViewModel.insertUser(user)
        }
    }

    companion object {
        const val USER_SIGN_IN = "user"
    }
}