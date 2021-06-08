package com.example.core.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MarkerEntity")
data class MarkerEntity(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "marker_id")
        var markerId: String = "",

        @ColumnInfo(name = "user_id")
        var userId: String = "",

        @ColumnInfo(name = "disaster_id")
        var disasterId: String = "",

        @ColumnInfo(name = "longitude")
        var longitude: Double = 0.00,

        @ColumnInfo(name = "latitude")
        var latitude: Double = 0.00,

        @ColumnInfo(name = "message")
        var message: String = "",

        @ColumnInfo(name = "voice_message_path")
        val voiceMessagePath: String = ""
)