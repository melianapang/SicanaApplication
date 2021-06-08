package com.example.core.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DisasterEntity")
data class DisasterEntity(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "location_name")
        val locationName: String,
        @ColumnInfo(name = "disaster_name")
        val disasterName: String,
        @ColumnInfo(name = "radius")
        val radius: Double,
        @ColumnInfo(name = "longitude")
        val longitude: Double,
        @ColumnInfo(name = "latitude")
        val latitude: Double
)