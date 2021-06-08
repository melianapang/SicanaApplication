package com.example.core.data.sharedpreference

import android.content.Context
import android.util.Log
import com.bumptech.glide.load.HttpException
import com.example.core.data.sharedpreference.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserPreference(context: Context) {
    companion object {
        private const val PREF_LOGGED_IN = "PREF_LOGGED_IN"
        private const val PREFS_NAME = "user_pref"
        private const val USER_ID = "user_id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PHONE_NUMBER = "phone"
        private const val PATH_PHOTO = "path_photo"
        private const val IS_VOLUNTEER = "isVolunteer"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setIsLoggedIn(isLoggedIn:Boolean){
        val editor = preferences.edit()
        editor.putBoolean(PREF_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun getIsLoggedIn():Boolean{
        return preferences.getBoolean(PREF_LOGGED_IN, false)
    }

    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(USER_ID, value.userId ?: "")
        editor.putString(NAME, value.name)
        editor.putString(EMAIL, value.email)
        editor.putString(PHONE_NUMBER, value.phoneNumber ?: "")
        editor.putString(PATH_PHOTO, value.pathPicture ?: "")
        editor.putBoolean(IS_VOLUNTEER, value.isVolunteer)
        editor.apply()
    }

    fun getUser(): Flow<UserModel> {
        return flow {
            try {
                val model = UserModel()
                model.userId = preferences.getString(USER_ID, "")
                model.name = preferences.getString(NAME, "")
                model.email = preferences.getString(EMAIL, "")
                model.phoneNumber = preferences.getString(PHONE_NUMBER, "")
                model.pathPicture = preferences.getString(PATH_PHOTO, "")
                model.isVolunteer = preferences.getBoolean(IS_VOLUNTEER, false)
                if(model.userId != "" && model.name != "" && model.email != "" && model.phoneNumber != "") emit(model)
            } catch (e: HttpException) {
                Log.e(PREFS_NAME, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}