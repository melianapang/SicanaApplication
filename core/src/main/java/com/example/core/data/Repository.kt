package com.example.core.data

import com.example.core.data.local.LocalDataSource
import com.example.core.data.remote.ApiResponse
import com.example.core.data.remote.RemoteDataSource
import com.example.core.data.remote.response.*
import com.example.core.data.sharedpreference.UserPreference
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.utils.MappingHelper
import com.example.core.valueobject.Resource
import kotlinx.coroutines.flow.*

class Repository (
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val userPreference: UserPreference
) : IRepository {

    //User
    override fun setIsLoggedIn(isLoggedIn: Boolean) {
        userPreference.setIsLoggedIn(isLoggedIn)
    }

    override fun getIsLoggedIn(): Boolean {
        return userPreference.getIsLoggedIn()
    }

    override fun getUserReference() : Flow<User>{
        return userPreference.getUser().map { MappingHelper.mapUserModelToUser(it) }
    }

    override suspend fun insertUser(user: User) {
        //set to api
        val userResponse = MappingHelper.mapUserToUserResponse(user)
       remoteDataSource.insertUser(userResponse)
    }

    override fun setUser(user: User) {
        //set to reference
        val userModel = MappingHelper.mapUserToUserModel(user)
        userPreference.setUser(userModel)
    }

    //Regis for volunteering
    override suspend fun regisVolunteer(clusterId: String,volunteer : Volunteer): Flow<Resource<out VolunteerResponse>> {
        val volunteerResponse = MappingHelper.mapVolunteerToVolunteerResponse(volunteer)
        return remoteDataSource.regisVolunteer(clusterId, volunteerResponse)
    }

    //Marker
    override suspend fun insertMarker(marker: Marker) : Flow<Resource<out MarkerResponse>> {
        val response = MappingHelper.mapMarkerToMarkerResponse(marker)
        val result = remoteDataSource.insertMarker(response)
        return result
    }

    //Disasters
    override fun getAllDisasters(): Flow<Resource<List<Disaster>>> {
        return object :
                NetworkBoundResource<List<Disaster>, List<DisasterResponse>>() {
            public override fun loadFromDB(): Flow<List<Disaster>> {
                return localDataSource.getAllDisasters().map { MappingHelper.mapDisasterEntityToDisaster(it) }
            }

            override fun shouldFetch(data: List<Disaster>?): Boolean =
                    data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<DisasterResponse>>> =
                    remoteDataSource.getAllDisasters()

            override suspend fun saveCallResult(data: List<DisasterResponse>) {
                val domain = MappingHelper.mapDisasterResponseToDisasterEntity(data)
                localDataSource.insertAllDisasters(domain)
            }
        }.asFlow()
    }

    //Clusters
    override fun getAllClusters(): Flow<Resource<List<Cluster>>> {
        return object :
            NetworkBoundResource<List<Cluster>, List<ClusterResponse>>() {
            public override fun loadFromDB(): Flow<List<Cluster>> {
                return localDataSource.getAllClusters().map { MappingHelper.mapClusterEntityToCluster(it) }
            }

            override fun shouldFetch(data: List<Cluster>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<ClusterResponse>>> =
                remoteDataSource.getAllClusters()

            override suspend fun saveCallResult(data: List<ClusterResponse>) {
                val domain = MappingHelper.mapClusterResponseToClusterEntity(data)
                localDataSource.insertAllClusters(domain)
            }
        }.asFlow()
    }

    //News
    override fun getAllNews(): Flow<Resource<List<News>>> {
        return object :
            NetworkBoundResource<List<News>, List<NewsResponse>>() {
            public override fun loadFromDB(): Flow<List<News>> {
                return localDataSource.getAllNews().map { MappingHelper.mapNewsEntityToNews(it) }
            }

            override fun shouldFetch(data: List<News>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<NewsResponse>>> =
                remoteDataSource.getAllNews()

            override suspend fun saveCallResult(data: List<NewsResponse>) {
                val domain = MappingHelper.mapNewsResponseToNewsEntity(data)
                localDataSource.insertAllNews(domain)
            }
        }.asFlow()
    }
}