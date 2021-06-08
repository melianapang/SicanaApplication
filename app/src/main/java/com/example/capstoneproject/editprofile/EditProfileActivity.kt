package com.example.capstoneproject.editprofile

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.net.toUri
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityEditProfileBinding
import com.example.capstoneproject.viewmodel.UserViewModel
import com.example.core.domain.model.User
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.koin.android.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var storageRef : StorageReference
    private lateinit var user: User
    private var isChanged : Boolean = false
    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        storageRef = FirebaseStorage.getInstance().getReference()

        setCurrentUser()

        setSupportBar()
        setProfilePictureButton()
        setEditText()
        setBackButton()
        setSaveButton()
    }

    private fun setSupportBar() {
        supportActionBar?.apply {
            customView = binding.toolbar
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setCurrentUser(){
        userViewModel.getUserReference().observe(this, {
            showLoading(true)
            if(it != null) {
               user = it
               setContent()
               showLoading(false)
            }
        })
    }

    private fun setContent(){
        with(binding){
            editName.setText(user.name, TextView.BufferType.EDITABLE)
            editEmail.setText(user.email, TextView.BufferType.EDITABLE)
            tvPhone.text = user.phoneNumber

            editName.setSelection(editName.text.toString().length)
            editEmail.setSelection(editEmail.text.toString().length)

            if(user.pathPicture.isNotEmpty()) {
                val imageRef = storageRef.child(user.pathPicture)
                imageRef.downloadUrl.addOnSuccessListener {
                    Glide.with(this@EditProfileActivity)
                            .load(it)
                            .into(imgProfile)
                }
            }
        }
    }

    private fun setBackButton(){
        binding.btnBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })
    }

    private fun setEditText(){
        with(binding){
            editName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    editName.error = null
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString() != user.name) isChanged = true
                    if(editName.text.toString().isEmpty()) editName.setError(resources.getString(R.string.empty_field)) else editName.setError(null)
                }
            })

            editEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    editEmail.error = null
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString() != user.email) isChanged = true
                    if(!isValidateEmail(s.toString())) editEmail.setError(resources.getString(R.string.wrong_format_email)) else editEmail.setError(null)
                    if(editEmail.text.toString().isEmpty()) editEmail.setError(resources.getString(R.string.empty_field)) else editEmail.setError(null)
                }
            })
        }
    }

    private fun setSaveButton(){
        binding.btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (isChanged) {
                    showLoading(true)
                    with(binding) {
                        if (editName.error == null && isValidateEmail(editEmail.text.toString())) {
                            user.name = editName.text.toString()
                            user.email = editEmail.text.toString()
                            user.phoneNumber = tvPhone.text.toString()

                            //upload profile picture to firebase
                            if(user.pathPicture.isNotEmpty()) {
                                val profileImagesRef = storageRef.child("images/" + user.phoneNumber + ".jpg")
                                profileImagesRef.putFile(user.pathPicture.toUri())
                                        .addOnSuccessListener {
                                            user.pathPicture = "images/" + user.phoneNumber + ".jpg"
                                            userViewModel.setUser(user)
                                        }
                                        .addOnFailureListener {
                                            showToast(resources.getString(R.string.failed_to_save_photo))
                                        }

                            }
                            else{
                                //set user to view model
                                userViewModel.setUser(user)
                            }

                            showLoading(false)
                            showFeedbackDialog(
                                true,
                                resources.getString(R.string.edit_profile_success)
                            )
                        } else {
                            showLoading(false)
                            showFeedbackDialog(false, resources.getString(R.string.empty_field))
                        }
                    }
                } else {
                    onBackPressed()
                }
            }
        })
    }

    private fun isValidateEmail(emailInput:String) : Boolean {
        return emailInput.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
    }

    private fun setProfilePictureButton(){
        binding.containerProfilePicture.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission denied
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery(){
        //Intent to pick an image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    showToast(resources.getString(R.string.permission_denied))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null){
            if(data.data.toString() != user.pathPicture) {
                isChanged = true
                user.pathPicture = data.data.toString()
                Glide.with(this)
                        .load(data.data)
                        .into(binding.imgProfile)
            }
        }
    }

    private fun showFeedbackDialog(isSuccess : Boolean, msg : String){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.feedback_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.tv_subtitle_dialog).text = msg

        if(isSuccess){
            dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility = View.VISIBLE
            dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility = View.INVISIBLE
            dialog.findViewById<TextView>(R.id.tv_title_dialog).text = resources.getString(R.string.success_string)
            dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
                dialog.dismiss()
                startActivity(Intent(this@EditProfileActivity,Class.forName("com.example.capstoneproject.main.MainActivity")))
            }
        }
        else{
            dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility = View.INVISIBLE
            dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility = View.VISIBLE
            dialog.findViewById<TextView>(R.id.tv_title_dialog).text = resources.getString(R.string.failed_string)
            dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    companion object{
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }
}