package com.example.capstoneproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.model.Cluster
import com.example.core.domain.usecase.IUseCase
import com.example.core.valueobject.Resource

class ClusterViewModel (private val useCase: IUseCase) : ViewModel()  {
    fun getAllClusters() : LiveData<Resource<List<Cluster>>> = useCase.getAllClusters().asLiveData()
}