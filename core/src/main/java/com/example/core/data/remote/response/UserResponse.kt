package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("phone_number")
	val phoneNumber: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)
