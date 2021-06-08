package com.example.capstoneproject.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentNotificationsBinding
import com.google.android.material.tabs.TabLayoutMediator

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding

    companion object {
        private val TAB_TITLES = intArrayOf(
                R.string.notification_tab,
                R.string.regist_tab
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        managePagerAdapter()
    }

    private fun managePagerAdapter() {
        val sectionsPagerAdapter = NewsSectionPagerAdapter(requireActivity() as AppCompatActivity)
        binding.viewPagerNotif.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayoutNotif, binding.viewPagerNotif) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}