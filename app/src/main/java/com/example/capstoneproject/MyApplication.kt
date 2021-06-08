package com.example.capstoneproject

import android.app.Application
import com.example.capstoneproject.di.useCaseModule
import com.example.capstoneproject.di.viewModelModule
import com.example.core.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                    listOf(
                            useCaseModule,
                            databaseUserModule,
                            networkModule,
                            repositoryModule,
                            viewModelModule
                    )
            )
        }
    }
}
