package com.example.core.data.sharedpreference.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var userId: String? = null,
    var name: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var pathPicture : String? = null,
    var isVolunteer: Boolean = false
) : Parcelable