package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News (
    var clusterId: String = "",
    var title: String ="",
    var description: String ="",
    var date: String = ""
) : Parcelable