package com.example.capstoneproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.model.Disaster
import com.example.core.domain.usecase.IUseCase
import com.example.core.valueobject.Resource

class DisasterViewModel (private val useCase: IUseCase) : ViewModel()  {
    fun getAllDisasters() : LiveData<Resource<List<Disaster>>> = useCase.getAllDisasters().asLiveData()
}