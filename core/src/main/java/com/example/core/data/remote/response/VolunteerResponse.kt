package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class VolunteerResponse(

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("cluster_id")
    val clusterId: String,
)
