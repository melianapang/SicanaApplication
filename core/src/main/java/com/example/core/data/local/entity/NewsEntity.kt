package com.example.core.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewsEntity")
data class NewsEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "cluster_id")
    val clusterId: String,
)