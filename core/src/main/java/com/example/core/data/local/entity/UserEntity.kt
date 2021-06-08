package com.example.core.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userEntity")
data class UserEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userId")
    var id: String ="",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "phone_number")
    var phone_number: String ="",

    @ColumnInfo(name = "path_picture")
    var pathPicture: String = "",

    @NonNull
    @ColumnInfo(name = "is_volunteer")
    var is_volunteer: Boolean = false
)