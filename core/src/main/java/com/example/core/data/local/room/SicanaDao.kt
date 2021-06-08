package com.example.core.data.local.room

import androidx.room.*
import com.example.core.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SicanaDao {
    //Disaster
    @Query("SELECT * FROM DisasterEntity")
    fun getAllDisasters(): Flow<List<DisasterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDisasters(cluster: List<DisasterEntity>)

    //Cluster
    @Query("SELECT * FROM ClusterEntity")
    fun getAllClusters(): Flow<List<ClusterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClusters(cluster: List<ClusterEntity>)

    //News
    @Query("SELECT * FROM NewsEntity")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(news: List<NewsEntity>)
}