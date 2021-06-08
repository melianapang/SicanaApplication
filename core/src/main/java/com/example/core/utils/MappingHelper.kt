package com.example.core.utils


import com.example.core.data.local.entity.*
import com.example.core.data.remote.response.*
import com.example.core.data.sharedpreference.model.UserModel
import com.example.core.domain.model.*


object MappingHelper {
    //User
    fun mapUserToUserResponse(input: User): UserResponse = UserResponse(
            id = input.userId,
            name = input.name,
            phoneNumber = input.phoneNumber,
            email = input.email
    )

    fun mapUserToUserModel(input: User): UserModel = UserModel(
            userId = input.userId,
            name = input.name,
            phoneNumber = input.phoneNumber,
            email = input.email,
            pathPicture = input.pathPicture,
            isVolunteer = input.isVolunteer
    )

    fun mapUserModelToUser(input: UserModel): User = User(
            userId = input.userId ?: "",
            name = input.name ?: "",
            phoneNumber = input.phoneNumber ?: "",
            email = input.email ?: "",
            pathPicture = input.pathPicture ?: "",
            isVolunteer = input.isVolunteer
    )

    //Regis for Volunteering
    fun mapVolunteerToVolunteerResponse(input: Volunteer): VolunteerResponse {
        return VolunteerResponse(
                userId = input.userId,
                clusterId = input.clusterId,
        )
    }

    //Marker
    fun mapMarkerToMarkerResponse(input: Marker): MarkerResponse {
        return MarkerResponse(
                userId = input.userId,
                disasterId = input.disasterId,
                longitude = input.longitude,
                latitude = input.latitude,
                message = input.message,
                voiceMessagePath = input.voiceMessagePath
        )
    }

    //Disaster
    fun mapDisasterResponseToDisasterEntity(input: List<DisasterResponse>): List<DisasterEntity> =
            input.map {
                DisasterEntity(
                        id = it.id,
                        longitude = it.longitude,
                        latitude = it.latitude,
                        radius = it.radius,
                        locationName = it.locationName,
                        disasterName = it.disasterName
                )
            }


    fun mapDisasterEntityToDisaster(input: List<DisasterEntity>): List<Disaster> =
            input.map {
                Disaster(
                        id = it.id,
                        longitude = it.longitude,
                        latitude = it.latitude,
                        radius = it.radius,
                        locationName = it.locationName,
                        disasterName = it.disasterName
                )
            }

    //Cluster
    fun mapClusterResponseToClusterEntity(input: List<ClusterResponse>): List<ClusterEntity> =
            input.map {
                ClusterEntity(
                        clusterNumber = it.clusterNumber,
                        longitude = it.longitude,
                        latitude = it.latitude,
                        radius = it.radius,
                        clusterId = it.id
                )
            }


    fun mapClusterEntityToCluster(input: List<ClusterEntity>): List<Cluster> =
            input.map {
                Cluster(
                        clusterId = it.clusterId,
                        longitude = it.longitude,
                        latitude = it.latitude,
                        radius = it.radius,
                        clusterNumber = it.clusterNumber
                )
            }


    //News
    fun mapNewsEntityToNews(input: List<NewsEntity>): List<News> =
            input.map {
                News(
                        title = it.title,
                        description = it.description,
                        date = it.date,
                        clusterId = it.clusterId
                )
            }


    fun mapNewsResponseToNewsEntity(input: List<NewsResponse>): List<NewsEntity> =
            input.map {
                NewsEntity(
                        title = it.title,
                        description = it.description,
                        date = it.date,
                        clusterId = it.id,
                )
            }

}

