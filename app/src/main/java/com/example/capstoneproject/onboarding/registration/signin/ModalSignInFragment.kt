package com.example.capstoneproject.onboarding.registration.signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentModalSignInBinding
import com.example.core.domain.model.User
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalSignInFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentModalSignInBinding
    private var isVolunteer = true

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun setEditText(){
        with(binding){
            editFullname.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(s?.trim().toString().isEmpty()) editFullname.error = resources.getString(R.string.field_empty) else editFullname.error = null
                }

                override fun afterTextChanged(s: Editable?) {
                    if(s?.trim().toString().isEmpty()) editFullname.error = resources.getString(R.string.field_empty) else editFullname.error =null
                }
            })

            editEmail.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(s?.trim().toString().isEmpty()) editEmail.error = resources.getString(R.string.field_empty) else editEmail.error = null
                }

                override fun afterTextChanged(s: Editable?) {
                    if(s?.trim().toString().isEmpty()) editEmail.error = resources.getString(R.string.field_empty) else editEmail.error = null
                    when(isValidateEmail(s.toString())){
                        false -> editEmail.error = resources.getString(R.string.wrong_format_email)
                        true -> editEmail.error = null
                    }
                }
            })
        }
    }

    private fun setButtons() {
        with(binding) {
            btnVolunteer.setOnClickListener {
                if (!isVolunteer) isVolunteer = !isVolunteer
                setButtonVolunteerActive()
            }

            btnUser.setOnClickListener {
                if (isVolunteer) {
                    setButtonVolunteerInactive()
                    isVolunteer = !isVolunteer
                }
            }

            btnCreateAccount.setOnClickListener {
                if (!checkboxPrivacyCheck.isChecked) {
                    showToast(resources.getString(R.string.privacy_check_is_not_checked))
                } else if (editFullname.error != null || editEmail.error != null || binding.editEmail.text.isEmpty() || binding.editFullname.text.isEmpty()) {
                    if(editFullname.text.isEmpty()) editFullname.error = resources.getString(R.string.field_empty)
                    if(editEmail.text.isEmpty()) editEmail.error = resources.getString(R.string.field_empty)
                    showToast(resources.getString(R.string.field_empty))
                } else {
                    val name = editFullname.text.toString()
                    val email = editEmail.text.toString()
                    val user = User(
                            name = name,
                            email = email,
                            isVolunteer = isVolunteer
                    )
                    onItemClickCallback?.onSendClicked(user)
                }
            }
        }
    }

    private fun setButtonVolunteerActive() {
        with(binding) {
            val volunteerIcon = ResourcesCompat.getDrawable(resources, R.drawable.volunteer_active, null)
            btnVolunteer.setCompoundDrawablesWithIntrinsicBounds(null, volunteerIcon, null,null)
            btnVolunteer.background =
                ResourcesCompat.getDrawable(resources, R.drawable.blue_button, null)
            btnVolunteer.setTextColor(ContextCompat.getColor(requireActivity(),android.R.color.white))

            val userIcon = ResourcesCompat.getDrawable(resources, R.drawable.user_inactive, null)
            btnUser.setCompoundDrawablesWithIntrinsicBounds(null, userIcon, null,null)
            btnUser.background =
                ResourcesCompat.getDrawable(resources, R.drawable.white_button, null)
            btnUser.setTextColor(ContextCompat.getColor(requireActivity(), R.color.blue_buttons))
        }
    }

    private fun setButtonVolunteerInactive() {
        with(binding) {
            val volunteerIcon = ResourcesCompat.getDrawable(resources, R.drawable.volunteer_inactive, null)
            btnVolunteer.setCompoundDrawablesWithIntrinsicBounds(null, volunteerIcon, null,null)
            btnVolunteer.background =
                ResourcesCompat.getDrawable(resources, R.drawable.white_button, null)
            btnVolunteer.setTextColor(ContextCompat.getColor(requireActivity(),R.color.blue_buttons))

            val userIcon = ResourcesCompat.getDrawable(resources, R.drawable.user_active, null)
            btnUser.setCompoundDrawablesWithIntrinsicBounds(null, userIcon, null,null)
            btnUser.background =
                ResourcesCompat.getDrawable(resources, R.drawable.blue_button, null)
            btnUser.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        }
    }

    private fun isValidateEmail(emailInput:String) : Boolean {
        return emailInput.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onSendClicked(user:User)
    }
}