package com.example.capstoneproject.main.news.registerVolunteer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentRegisVolunteerBinding
import com.example.capstoneproject.main.MainActivity
import com.example.capstoneproject.utils.customSpinner.CustomSpinner
import com.example.capstoneproject.viewmodel.ClusterViewModel
import com.example.capstoneproject.viewmodel.UserViewModel
import com.example.capstoneproject.viewmodel.VolunteerViewModel
import com.example.core.domain.model.Cluster
import com.example.core.domain.model.Volunteer
import com.example.core.presentation.SpineerClusterAdapter
import com.example.core.valueobject.Status
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class RegisForVolunteerFragment : Fragment(), CustomSpinner.OnSpinnerEventsListener {
    private lateinit var binding : FragmentRegisVolunteerBinding
    private lateinit var idUser : String
    private val userViewModel : UserViewModel by viewModel()
    private val clusterViewModel : ClusterViewModel by viewModel()
    private val volunteerViewModel : VolunteerViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisVolunteerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getUserReference().observe(requireActivity(),{ user ->
            if (user != null) {
                with(binding){
                    tvNameContent.text = user.name
                    tvEmailContent.text = user.email
                    tvPhoneContent.text = user.phoneNumber
                }
                idUser = user.userId
            }
        })

        setSpinner()
        setButton()
    }

    private fun setSpinner() : List<Cluster> {
        val list = arrayListOf<Cluster>()
        clusterViewModel.getAllClusters().observe(requireActivity(), { cluster ->
            if (cluster != null && cluster.status == Status.SUCCESS) {
                if (cluster.data != null) {
                    list.addAll(cluster.data!!)
                    val adapter = SpineerClusterAdapter(requireActivity(), cluster.data!!)
                    binding.spinnerNewsCluster.adapter = adapter
                }
            }
        })

        return list
    }

    private fun setButton() {
        binding.btnRegistForVolunteer.setOnClickListener {
            if(!binding.checkboxApproval.isChecked){
                showToast(resources.getString(R.string.approval_check_is_not_checked))
            }
            else{
                val selectedCluster = binding.spinnerNewsCluster.selectedItem
                //get Cluster Id
                clusterViewModel.getAllClusters().observe(requireActivity(), { listCluster ->
                    if (listCluster != null && listCluster.status == Status.SUCCESS) {
                        if (listCluster.data != null) {
                            val idSelectedCluster = listCluster.data!!.get(selectedCluster.toString().toInt()).clusterId
                            // API
                            lifecycleScope.launch {
                                volunteerViewModel.regisVolunteer(idSelectedCluster, Volunteer(userId = idUser, clusterId = idSelectedCluster)).observe(requireActivity(),{ regis ->
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
                })
            }
        }
    }

    private fun showFeedbackDialog(isSuccess : Boolean, msg : String?){
        val dialog = Dialog(requireActivity())
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
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
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
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    //Custom Spinner
    override fun onPopupWindowOpened(spinner: Spinner?) {
        binding.spinnerNewsCluster.background = ResourcesCompat.getDrawable(resources, R.drawable.spinner_bg_up, null)
    }

    override fun onPopupWindowClosed(spinner: Spinner?) {
        binding.spinnerNewsCluster.background = ResourcesCompat.getDrawable(resources, R.drawable.spinner_bg, null)
    }
}