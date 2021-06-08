package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disaster (
        val id: String = "",
        val locationName: String = "",
        val disasterName: String = "",
        val longitude: Double = 0.00,
        val latitude: Double = 0.00,
        val radius: Double = 0.00
) : Parcelable