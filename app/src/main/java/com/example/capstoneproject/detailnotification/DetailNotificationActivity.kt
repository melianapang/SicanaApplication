package com.example.capstoneproject.detailnotification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.databinding.ActivityDetailNotificationBinding
import com.example.capstoneproject.detailnotification.registerVolunteer.RegisForVolunteerNewsActivity

class DetailNotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNotificationBinding
    private lateinit var dateNotif: String
    private lateinit var titleNotif: String
    private lateinit var descNotif : String
    private lateinit var clusterIdNotif: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if(bundle != null){
            with(binding){
                dateNotif = bundle.getString(DATE_NOTIF).toString()
                titleNotif = bundle.getString(TITLE_NOTIF).toString()
                descNotif = bundle.getString(DESC_NOTIF).toString()
                clusterIdNotif = bundle.getString(CLUSTER_ID_NOTIF).toString()
                tvContentDate.text = dateNotif
                tvContentJudulNotification.text = titleNotif
                tvContentNotification.text = descNotif
                println("SEND "+ clusterIdNotif)
            }
        }

        setBackButton()
        setButton()
    }

    private fun setButton() {
        binding.btnRegistForVolunteer.setOnClickListener {
            val intent = Intent(this, RegisForVolunteerNewsActivity::class.java)
            intent.putExtra(DATE_NOTIF, dateNotif)
            intent.putExtra(TITLE_NOTIF, titleNotif)
            intent.putExtra(DESC_NOTIF, descNotif)
            intent.putExtra(CLUSTER_ID_NOTIF, clusterIdNotif)
            startActivity(intent)
        }
    }

    private fun setBackButton(){
        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    companion object{
        const val DATE_NOTIF = "date_notif"
        const val TITLE_NOTIF = "title_notif"
        const val DESC_NOTIF = "desc_notif"
        const val CLUSTER_ID_NOTIF = "cluster_id_notif"
    }
}