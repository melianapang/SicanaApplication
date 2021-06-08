package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Volunteer(
    val userId: String,
    val clusterId: String,
): Parcelable
