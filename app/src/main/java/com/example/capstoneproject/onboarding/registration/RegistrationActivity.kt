package com.example.capstoneproject.onboarding.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstoneproject.databinding.ActivityRegistrationBinding
import com.example.capstoneproject.main.MainActivity
import com.example.capstoneproject.main.MainActivity.Companion.USER_SIGN_IN
import com.example.capstoneproject.onboarding.registration.authentication.SendOtpModalFragment
import com.example.capstoneproject.onboarding.registration.authentication.VerifyOtpModalFragment
import com.example.capstoneproject.onboarding.registration.signin.ModalSignInFragment
import com.example.core.domain.model.User
import com.google.firebase.auth.PhoneAuthProvider

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSignInPage()
    }

    private fun setSignInPage() {
        val fragment = ModalSignInFragment()
        fragment.show(supportFragmentManager, "SignInPage")
        fragment.setOnItemClickCallback(object : ModalSignInFragment.OnItemClickCallback {
            override fun onSendClicked(user: User) {
                fragment.dismiss()
                setSendOTPPage(user)
            }
        })
    }

    private fun setSendOTPPage(user: User) {
        val fragment = SendOtpModalFragment()
        fragment.show(supportFragmentManager, "SendOtpPage")
        fragment.setOnItemClickCallback(object : SendOtpModalFragment.OnItemClickCallback {
            override fun onItemClicked(
                phoneNumber: String,
                verificationId: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                fragment.dismiss()
                user.phoneNumber = phoneNumber
                setVerificationPage(user, verificationId, forceResendingToken)
            }
        })
    }

    private fun setVerificationPage(
        user: User,
        verificationId: String,
        forceResendingToken: PhoneAuthProvider.ForceResendingToken
    ) {
        val fragment = VerifyOtpModalFragment()
        fragment.setItemsOtp(user, verificationId, forceResendingToken)
        fragment.show(supportFragmentManager, "VerifyOtpPage")
        fragment.setOnItemClickCallback(object : VerifyOtpModalFragment.OnItemClickCallback {
            override fun onItemClicked(userThis: User) {
                fragment.dismiss()
                val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                intent.putExtra(USER_SIGN_IN, userThis)
                startActivity(intent)
            }
        })
    }
}
