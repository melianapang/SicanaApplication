package com.example.capstoneproject.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.capstoneproject.databinding.ActivitySplashScreenBinding
import com.example.capstoneproject.main.MainActivity
import com.example.capstoneproject.onboarding.intro.OnboardingActivity
import com.example.capstoneproject.viewmodel.UserViewModel
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val userViewModel : UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLoggedIn = userViewModel.getIsLoggedIn()
            Log.d("isLoggedIn", isLoggedIn.toString())
            when(isLoggedIn){
                true -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                false -> {
                    val intent = Intent(this, OnboardingActivity::class.java)
                    startActivity(intent)
                }
            }
            finish()
        }, 1000)
    }
}