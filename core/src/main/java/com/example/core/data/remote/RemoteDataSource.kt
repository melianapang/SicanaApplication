package com.example.core.data.remote

import android.util.Log
import com.bumptech.glide.load.HttpException
import com.example.core.data.remote.api.ApiEndPoint
import com.example.core.data.remote.response.*
import com.example.core.valueobject.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiEndPoint) {

    //User
    suspend fun insertUser(userResponse: UserResponse) {
         apiService.insertUser(userResponse)
    }

    //Regis for volunteering
    suspend fun regisVolunteer(clusterId : String, volunteerResponse: VolunteerResponse): Flow<Resource<out VolunteerResponse>> {
        return flow {
            try {
                val response = apiService.regisVolunteer(clusterId, volunteerResponse)
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Resource.success(responseBody))
                }
            } catch (e: HttpException) {
                emit(Resource.error(e.message, null))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    //Marker
    suspend fun insertMarker(markerResponse: MarkerResponse) : Flow<Resource<out MarkerResponse>> {
        return flow {
            try {
                val response = apiService.insertMarker(markerResponse)
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Resource.success(responseBody))
                }
            } catch (e: HttpException) {
                emit(Resource.error(e.message, null))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    //Disaster
    suspend fun getAllDisasters() : Flow<ApiResponse<List<DisasterResponse>>> {
        return flow {
            try {
                val response = apiService.getAllDisasters()
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ApiResponse.Success(responseBody.res))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: HttpException) {
                emit(ApiResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    //Clusters
    fun getAllClusters(): Flow<ApiResponse<List<ClusterResponse>>> {
        return flow {
            try {
                val response = apiService.getAllClusters()
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ApiResponse.Success(responseBody.res))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: HttpException) {
                emit(ApiResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    //News
    fun getAllNews(): Flow<ApiResponse<List<NewsResponse>>> {
        return flow {
            try {
                val response = apiService.getAllNews()
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ApiResponse.Success(responseBody.res))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: HttpException) {
                emit(ApiResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private const val TAG = "RemoteDataSource"
    }
}