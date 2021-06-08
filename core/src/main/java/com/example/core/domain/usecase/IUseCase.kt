package com.example.core.domain.usecase

import com.example.core.data.remote.response.MarkerResponse
import com.example.core.data.remote.response.VolunteerResponse
import com.example.core.domain.model.*
import com.example.core.valueobject.Resource
import kotlinx.coroutines.flow.Flow

interface IUseCase {
    //User
    fun setUser(user: User)
    fun setIsLoggedIn(isLoggedIn:Boolean)
    fun getIsLoggedIn():Boolean
    fun getUserReference(): Flow<User>
    suspend fun insertUser(user: User)

    //Regis for Volunteering
    suspend fun regisVolunteer(clusterId: String,volunteer: Volunteer) : Flow<Resource<out VolunteerResponse>>

    //Marker
    suspend fun insertMarker(marker: Marker) : Flow<Resource<out MarkerResponse>>

    //Disaster
    fun getAllDisasters(): Flow<Resource<List<Disaster>>>

    //Cluster
    fun getAllClusters(): Flow<Resource<List<Cluster>>>

    //News
    fun getAllNews(): Flow<Resource<List<News>>>
}