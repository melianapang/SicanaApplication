package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseNews(

	@field:SerializedName("res")
	val res: List<NewsResponse>
)

data class NewsResponse(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("cluster_id")
	val clusterId: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String
)
