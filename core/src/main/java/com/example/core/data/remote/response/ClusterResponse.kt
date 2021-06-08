package com.example.core.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseCluster(
	@SerializedName("res")
	val res: List<ClusterResponse>
)

@Parcelize
data class ClusterResponse(

	@SerializedName("cluster_number")
	val clusterNumber: Int,

	@SerializedName("Marker_id")
	val MarkerId: String,

	@SerializedName("latitude")
	val latitude: Double,

	@SerializedName("id")
	val id: String,

	@SerializedName("radius")
	val radius: Double,

	@SerializedName("longitude")
	val longitude: Double
): Parcelable
