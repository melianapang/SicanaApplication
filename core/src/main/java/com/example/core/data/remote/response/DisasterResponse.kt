package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseDisaster(

	@field:SerializedName("res")
	val res: List<DisasterResponse>
)

data class DisasterResponse(

	@field:SerializedName("location_name")
	val locationName: String,

	@field:SerializedName("disaster_name")
	val disasterName: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("radius")
	val radius: Double,

	@field:SerializedName("longitude")
	val longitude: Double
)
