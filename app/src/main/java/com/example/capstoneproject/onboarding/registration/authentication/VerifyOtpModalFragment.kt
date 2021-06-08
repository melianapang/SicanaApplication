package com.example.capstoneproject.onboarding.registration.authentication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentVerifyOtpModalBinding
import com.example.core.domain.model.User
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

class VerifyOtpModalFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentVerifyOtpModalBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userThis : User
    private lateinit var verificationId : String
    private lateinit var forceResendingToken : PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyOtpModalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.tvSubtitleOtp.text = resources.getString(R.string.subtitle_otp, userThis.phoneNumber)

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
        setInputCodes()
        setButtons()
    }

    private fun setButtons() {
        binding.btnNext.setOnClickListener{
            val inputCode1 = binding.editInputCode1.text.toString()
            val inputCode2 = binding.editInputCode2.text.toString()
            val inputCode3 = binding.editInputCode3.text.toString()
            val inputCode4 = binding.editInputCode4.text.toString()
            val inputCode5 = binding.editInputCode5.text.toString()
            val inputCode6 = binding.editInputCode6.text.toString()
            if(inputCode1.isEmpty() || inputCode2.isEmpty() || inputCode3.isEmpty() || inputCode4.isEmpty() || inputCode5.isEmpty() || inputCode6.isEmpty()){
                showFeedbackDialog(false, resources.getString(R.string.field_empty))
            }
            else {
                val codeString = StringBuilder().append(inputCode1).append(inputCode2).append(inputCode3).append(inputCode4).append(inputCode5).append(inputCode6).toString()
                verifyPhoneWithCode(verificationId, codeString)
            }
        }

        binding.tvBtnResend.setOnClickListener {
            resendVerificationCode(userThis.phoneNumber, forceResendingToken)
        }
    }

    private fun setInputCodes(){
        with(binding){
            editInputCode1.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        editInputCode2.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editInputCode2.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        editInputCode3.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editInputCode3.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        editInputCode4.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editInputCode4.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        editInputCode5.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editInputCode5.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        editInputCode6.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }


    private fun resendVerificationCode(phoneNumber: String, token : PhoneAuthProvider.ForceResendingToken){
        showLoading(true)

        val phoneNum = "+62" + phoneNumber
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNum)
                .setTimeout(300L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(mCallBacks)
                .setForceResendingToken(token)
                .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneWithCode(verificationId: String, code: String){
        showLoading(true)

        val credential = PhoneAuthProvider.getCredential(verificationId,code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential : PhoneAuthCredential) {
        showLoading(false)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val uid = firebaseAuth.currentUser?.uid
                if(uid != null) {
                    userThis.userId = uid
                    Log.d("TAG", "signInWithCredential:success")
                    showToast(resources.getString(R.string.logged_in_as, if(userThis.isVolunteer) resources.getString(R.string.volunteer_string) else resources.getString(R.string.user_string)))
                    onItemClickCallback?.onItemClicked(userThis)
                }
            }
        }.addOnFailureListener {
            //failed to sign in
            showToast(resources.getString(R.string.sign_in_failed))
        }
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
        }
    }

    private fun showFeedbackDialog(isSuccess : Boolean, msg : String){
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.feedback_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<TextView>(R.id.tv_title_dialog).text = if (isSuccess) resources.getString(R.string.success_string) else resources.getString(R.string.failed_string)
        dialog.findViewById<TextView>(R.id.tv_subtitle_dialog).text = msg
        dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
            dialog.dismiss()
            if(isSuccess) startActivity(Intent(requireActivity(),Class.forName("com.example.capstoneproject.main.MainActivity")))
        }
        dialog.show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    fun setItemsOtp(user : User, verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken){
        this.userThis = user
        this.verificationId = verificationId
        this.forceResendingToken = forceResendingToken
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(user : User)
    }
}