package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MarkerResponse(
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("disaster_id")
        val disasterId: String,
        @SerializedName("longitude")
        val longitude: Double,
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("message")
        val message: String,
        @SerializedName("voice_message_path")
        val voiceMessagePath: String
)