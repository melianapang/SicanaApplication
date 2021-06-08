package com.example.capstoneproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.model.User
import com.example.core.domain.usecase.IUseCase

class UserViewModel (private val useCase: IUseCase) : ViewModel() {
    fun getIsLoggedIn() : Boolean = useCase.getIsLoggedIn()
    fun setIsLoggedIn(isLoggedIn:Boolean) = useCase.setIsLoggedIn(isLoggedIn)
    fun setUser(user: User) = useCase.setUser(user)
    suspend fun insertUser(user: User) = useCase.insertUser(user)
    fun getUserReference(): LiveData<User> = useCase.getUserReference().asLiveData()
}