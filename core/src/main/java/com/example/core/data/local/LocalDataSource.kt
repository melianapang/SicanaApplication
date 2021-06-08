package com.example.core.data.local

import com.example.core.data.local.entity.*
import com.example.core.data.local.room.SicanaDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val sicanaDao: SicanaDao) {
    //Disaster
    fun getAllDisasters(): Flow<List<DisasterEntity>> = sicanaDao.getAllDisasters()
    suspend fun insertAllDisasters(disaster : List<DisasterEntity>) = sicanaDao.insertAllDisasters(disaster)

    //Cluster
    suspend fun insertAllClusters(cluster : List<ClusterEntity>) = sicanaDao.insertAllClusters(cluster)
    fun getAllClusters(): Flow<List<ClusterEntity>> = sicanaDao.getAllClusters()

    //News
    suspend fun insertAllNews(news : List<NewsEntity>) = sicanaDao.insertAllNews(news)
    fun getAllNews(): Flow<List<NewsEntity>> = sicanaDao.getAllNews()
}