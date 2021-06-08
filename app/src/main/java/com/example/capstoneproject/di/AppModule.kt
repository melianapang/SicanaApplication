package com.example.capstoneproject.di

import com.example.capstoneproject.viewmodel.*
import com.example.core.domain.usecase.IUseCase
import com.example.core.domain.usecase.Interactor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<IUseCase> { Interactor(get()) }
}

val viewModelModule = module {
    viewModel { MarkerViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { ClusterViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { DisasterViewModel(get()) }
    viewModel { VolunteerViewModel(get()) }
}