package com.example.capstoneproject.main.news

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capstoneproject.main.news.notification.NotificationFragment
import com.example.capstoneproject.main.news.registerVolunteer.RegisForVolunteerFragment

class NewsSectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = NotificationFragment()
            1 -> fragment = RegisForVolunteerFragment()
        }
        return fragment as Fragment
    }
}