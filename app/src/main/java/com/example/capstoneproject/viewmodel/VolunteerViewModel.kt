package com.example.capstoneproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.data.remote.response.VolunteerResponse
import com.example.core.domain.model.Volunteer
import com.example.core.domain.usecase.IUseCase
import com.example.core.valueobject.Resource

class VolunteerViewModel (private val useCase: IUseCase) : ViewModel()  {
    suspend fun regisVolunteer(clusterId: String, volunteer: Volunteer) : LiveData<Resource<out VolunteerResponse>> = useCase.regisVolunteer(clusterId, volunteer).asLiveData()
}