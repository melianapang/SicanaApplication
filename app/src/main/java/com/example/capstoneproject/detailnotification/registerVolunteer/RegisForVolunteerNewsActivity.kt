package com.example.capstoneproject.detailnotification.registerVolunteer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityRegisForVolunteerNewsBinding
import com.example.capstoneproject.detailnotification.DetailNotificationActivity
import com.example.capstoneproject.main.MainActivity
import com.example.capstoneproject.viewmodel.UserViewModel
import com.example.capstoneproject.viewmodel.VolunteerViewModel
import com.example.core.domain.model.Volunteer
import com.example.core.valueobject.Status
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class RegisForVolunteerNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisForVolunteerNewsBinding
    private lateinit var userId : String
    private lateinit var idCluster : String
    private val userViewModel : UserViewModel by viewModel()
    private val volunteerViewModel : VolunteerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisForVolunteerNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.getUserReference().observe(this,{
            userId = it.userId
            with(binding){
                tvNameContent.text = it.name
                tvEmailContent.text = it.email
                tvPhoneContent.text = it.phoneNumber
            }
        })

        val bundle = intent.extras
        if(bundle != null){
            idCluster = bundle.getString(DetailNotificationActivity.CLUSTER_ID_NOTIF) ?: ""
            println(idCluster)
            with(binding){
                tvDateContent.text = bundle.getString(DetailNotificationActivity.DATE_NOTIF)
                tvDescContent.text = bundle.getString(DetailNotificationActivity.DESC_NOTIF)
                tvNewsContent.text = bundle.getString(DetailNotificationActivity.TITLE_NOTIF)
            }
        }

        setBackButton()
        setButton()
    }

    private fun setButton() {
        binding.btnRegistForVolunteer.setOnClickListener {
            if(!binding.checkboxApproval.isChecked){
                showToast(resources.getString(R.string.approval_check_is_not_checked))
            }
            else{
                // API
                lifecycleScope.launch {
                    volunteerViewModel.regisVolunteer(idCluster, Volunteer(userId = userId, clusterId = idCluster)).observe(this@RegisForVolunteerNewsActivity,{ regis ->
                        if (regis != null) {
                            when (regis.status) {
                                Status.SUCCESS -> if (regis.data != null && regis.data!!.toString() != "") {
                                    showFeedbackDialog(true, resources.getString(R.string.regis_volunteer_success))
                                }
                                Status.LOADING -> showFeedbackDialog(true, null)
                                Status.ERROR -> {
                                    showFeedbackDialog(false, resources.getString(R.string.send_information_failed))
                                }
                            }
                        }
                    })
                }

            }
        }
    }

    private fun setBackButton(){
        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private fun showFeedbackDialog(isSuccess : Boolean, msg : String?){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.feedback_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if(msg == null){
            dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility = View.INVISIBLE
            dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility = View.INVISIBLE
            dialog.findViewById<TextView>(R.id.tv_subtitle_dialog).visibility = View.INVISIBLE
            dialog.findViewById<TextView>(R.id.tv_title_dialog).text = resources.getString(R.string.loading_striing)
        }
        else {
            dialog.findViewById<TextView>(R.id.tv_subtitle_dialog).text = msg
            if (isSuccess) {
                dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility =
                        View.VISIBLE
                dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility =
                        View.INVISIBLE
                dialog.findViewById<TextView>(R.id.tv_title_dialog).text =
                        resources.getString(R.string.success_string)
                dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
                    dialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility =
                        View.INVISIBLE
                dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility = View.VISIBLE
                dialog.findViewById<TextView>(R.id.tv_title_dialog).text =
                        resources.getString(R.string.failed_string)
                dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}