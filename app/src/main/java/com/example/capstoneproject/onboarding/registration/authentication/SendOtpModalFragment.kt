package com.example.capstoneproject.onboarding.registration.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentSendOtpModalBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class SendOtpModalFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSendOtpModalBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendOtpModalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        if (dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
            behaviour.isHideable = false
            behaviour.isFitToContents = true
            behaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
        setEditText()
        setButtons()
    }

    //pengecekan apakah no telp sudah terdaftar? pakai view model
    private fun setEditText() {
        with(binding){
            editPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun setButtons() {
        with(binding) {
            btnNext.setOnClickListener {
                if (editPhone.text.toString().isEmpty()) {
                    showToast(resources.getString(R.string.field_empty))
                } else {
                    btnNext.visibility = View.INVISIBLE
                    showLoading(true)
                    startPhoneAuthentication()
                }
            }
        }
    }

    private fun startPhoneAuthentication() {
        val phoneNum = "+62" + binding.editPhone.text.toString()
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(mCallBacks)
                .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            showLoading(false)
            binding.btnNext.visibility = View.VISIBLE
//            signInWithPhoneAuthCredential(p0)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            showLoading(false)
            binding.btnNext.visibility = View.VISIBLE
            showToast(e.message.toString())
        }

        override fun onCodeSent(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
            showLoading(false)
            binding.btnNext.visibility = View.VISIBLE
            //hide this modal, show code layout
            onItemClickCallback?.onItemClicked(binding.editPhone.text.toString(), verificationId, forceResendingToken)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(phoneNumber: String, verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken)
    }
}