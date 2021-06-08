package com.example.capstoneproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.model.News
import com.example.core.domain.usecase.IUseCase
import com.example.core.valueobject.Resource

class NewsViewModel(private val useCase: IUseCase) : ViewModel() {
    fun getAllNews() : LiveData<Resource<List<News>>> = useCase.getAllNews().asLiveData()
}