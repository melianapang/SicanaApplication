package com.example.core.domain.usecase

import com.example.core.data.remote.response.MarkerResponse
import com.example.core.data.remote.response.VolunteerResponse
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.valueobject.Resource
import kotlinx.coroutines.flow.Flow

class Interactor(private val repository: IRepository) : IUseCase {
    //User
    override fun setUser(user: User) = repository.setUser(user)
    override fun setIsLoggedIn(isLoggedIn: Boolean) = repository.setIsLoggedIn(isLoggedIn)
    override fun getIsLoggedIn(): Boolean = repository.getIsLoggedIn()
    override fun getUserReference(): Flow<User> = repository.getUserReference()
    override suspend fun insertUser(user: User) = repository.insertUser(user)

    //Regis for Volunteering
    override suspend fun regisVolunteer(clusterId: String, volunteer: Volunteer): Flow<Resource<out VolunteerResponse>> = repository.regisVolunteer(clusterId, volunteer)

    //Marker
    override suspend fun insertMarker(marker: Marker) : Flow<Resource<out MarkerResponse>> = repository.insertMarker(marker)

    //Disaster
    override fun getAllDisasters(): Flow<Resource<List<Disaster>>> = repository.getAllDisasters()

    //Clusters
    override fun getAllClusters(): Flow<Resource<List<Cluster>>> = repository.getAllClusters()

    //News
    override fun getAllNews(): Flow<Resource<List<News>>> = repository.getAllNews()
}