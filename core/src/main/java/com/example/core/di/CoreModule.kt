package com.example.core.di

import androidx.room.Room
import com.example.core.data.Repository
import com.example.core.data.local.LocalDataSource
import com.example.core.data.remote.RemoteDataSource
import com.example.core.data.remote.api.ApiEndPoint
import com.example.core.data.sharedpreference.UserPreference
import com.example.core.data.local.room.SicanaDatabase
import com.example.core.domain.repository.IRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseUserModule = module {
    factory { get<SicanaDatabase>().sicanaDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
                SicanaDatabase::class.java, "SicanaDB.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://famous-sandbox-314911.et.r.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiEndPoint::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single { UserPreference(androidContext()) }
    single<IRepository> {
        Repository(
            get(),
            get(),
            get()
        )
    }
}