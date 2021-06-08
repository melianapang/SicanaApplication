package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var userId: String ="",
    var name: String ="",
    var email: String ="",
    var phoneNumber: String = "",
    var pathPicture : String = "",
    var isVolunteer: Boolean = false
) : Parcelable