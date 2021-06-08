package com.example.capstoneproject.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentProfileViewBinding
import com.example.capstoneproject.editprofile.EditProfileActivity
import com.example.capstoneproject.splash.SplashScreenActivity
import com.example.capstoneproject.viewmodel.UserViewModel
import com.example.core.domain.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileViewFragment : Fragment() {
    private lateinit var binding: FragmentProfileViewBinding
    private val userViewModel: UserViewModel by viewModel()
    private lateinit var storageRef : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())

        storageRef = FirebaseStorage.getInstance().getReference()
        setUserAndContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setButtons()
    }

    private fun setUserAndContent(){
        userViewModel.getUserReference().observe(requireActivity(), {
            showLoading(true)
            if(it != null) {
                val user: User = it
                with(binding){
                    tvName.text = user.name
                    tvEmail.text = user.email
                    tvPhone.text = user.phoneNumber

                    if(user.pathPicture.isNotEmpty()) {
                        val imageRef = storageRef.child(user.pathPicture)
                        imageRef.downloadUrl.addOnSuccessListener {
                            Glide.with(requireActivity())
                                    .load(it)
                                    .into(binding.imgProfile)
                        }
                    }
                }
                showLoading(false)
            }
        })
    }

    private fun setButtons() {
        with(binding) {
            btnEdit.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                    startActivity(intent)
                }
            })

            btnLogOut.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    setLogOutModal()
                }
            })
        }
    }

    private fun setLogOutModal(){
        val bottomSheetDialogLogOut =
            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView =
            LayoutInflater.from(requireContext()).inflate(R.layout.item_log_out, null)

        bottomSheetView.findViewById<Button>(R.id.btn_yes)
            .setOnClickListener {
                userViewModel.setIsLoggedIn(false)
                userViewModel.setUser(User("", "", "", "", "", false))
                startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
            }

        bottomSheetView.findViewById<Button>(R.id.btn_no)
            .setOnClickListener { bottomSheetDialogLogOut.dismiss() }

        bottomSheetDialogLogOut.setContentView(bottomSheetView)
        bottomSheetDialogLogOut.show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }
}