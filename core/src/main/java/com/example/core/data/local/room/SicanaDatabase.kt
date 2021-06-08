package com.example.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.local.entity.*

@Database(
    entities = [UserEntity::class, ClusterEntity::class, NewsEntity::class, MarkerEntity::class, DisasterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SicanaDatabase : RoomDatabase() {
    abstract fun sicanaDao(): SicanaDao
}