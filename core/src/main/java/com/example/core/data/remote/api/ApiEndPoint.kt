package com.example.core.data.remote.api

import com.example.core.data.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiEndPoint {
    //Users
    @POST("users")
    suspend fun insertUser(@Body userResponse: UserResponse)

    //Marker
    @POST("markers")
    suspend fun insertMarker(@Body markerResponse: MarkerResponse) : Response<MarkerResponse>

    //Disaster
    @GET("disasters")
    suspend fun getAllDisasters():Response<ResponseDisaster>

    //Clusters
    @GET("clusters")
    suspend fun getAllClusters():Response<ResponseCluster>

    //News
    @GET("news")
    suspend fun getAllNews():Response<ResponseNews>

    //Regis for Volunteers
    @POST("volunteers/{cluster_id}")
    suspend fun regisVolunteer(@Path("cluster_id") cluster_id: String, @Body volunteerResponse: VolunteerResponse) : Response<VolunteerResponse>
}