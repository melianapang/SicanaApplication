package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Marker (
        var userId: String = "",
        var disasterId: String = "",
        var longitude: Double = 0.00,
        var latitude: Double = 0.00,
        var message : String = "",
        val voiceMessagePath: String =""
) : Parcelable