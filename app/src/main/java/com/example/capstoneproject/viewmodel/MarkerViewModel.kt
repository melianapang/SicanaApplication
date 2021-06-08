package com.example.capstoneproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.data.remote.response.MarkerResponse
import com.example.core.domain.model.Marker
import com.example.core.domain.usecase.IUseCase
import com.example.core.valueobject.Resource

class MarkerViewModel (private val useCase: IUseCase) : ViewModel()  {
    suspend fun insertMarker(marker: Marker) : LiveData<Resource<out MarkerResponse>> = useCase.insertMarker(marker).asLiveData()
}