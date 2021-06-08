package com.example.capstoneproject.onboarding.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityOnboardingBinding
import com.example.capstoneproject.onboarding.registration.RegistrationActivity

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setAdapter()
        setUpIndicators()
        setCurrentIndicator(0)
        setNextButton(0)
        setSkipButton()
    }

    private fun setAdapter() {
        adapter = OnboardingItemsAdapter(OnboardingObject.generateObject())
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setNextButton(position)
                    setCurrentIndicator(position)
                }
            }
        )
        (binding.viewPager.getChildAt(0) as RecyclerView).onScrollStateChanged(RecyclerView.OVER_SCROLL_NEVER)
    }

    private fun setUpIndicators() {
        val indicators = arrayOfNulls<ImageView>(adapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.inactive_indicator
                    )
                )
                it.layoutParams = layoutParams
                binding.indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childIndicatorsCount = binding.indicatorsContainer.childCount
        for (i in 0 until childIndicatorsCount) {
            val imgView = binding.indicatorsContainer.getChildAt(i) as ImageView
            if (position == i) {
                imgView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_indicator
                    )
                )
            } else {
                imgView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.inactive_indicator
                    )
                )
            }
        }
    }

    private fun setNextButton(position: Int) {
        val indicatorsCount = binding.indicatorsContainer.childCount
        when (position) {
            indicatorsCount - 1 -> {
                binding.btnNext.text = resources.getString(R.string.start_string)
                binding.btnNext.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View?) {
                        val intent = Intent(
                            this@OnboardingActivity, RegistrationActivity::class.java
                        )
                        startActivity(intent)
                    }
                })
            }
            else -> {
                binding.btnNext.text = resources.getString(R.string.next_string)
                binding.btnNext.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View?) {
                        binding.viewPager.setCurrentItem(position + 1)
                    }
                })
            }
        }
    }

    private fun setSkipButton() {
        binding.btnSkip.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent = Intent(
                    this@OnboardingActivity, RegistrationActivity::class.java
                )
                startActivity(intent)
            }
        })
    }
}