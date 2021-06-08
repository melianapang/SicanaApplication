package com.example.core.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ClusterEntity")
data class ClusterEntity(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        val clusterId: String,
        @ColumnInfo(name = "cluster_number")
        val clusterNumber: Int,
        @ColumnInfo(name = "longitude")
        val longitude: Double,
        @ColumnInfo(name = "latitude")
        val latitude: Double,
        @ColumnInfo(name = "radius")
        val radius: Double
)