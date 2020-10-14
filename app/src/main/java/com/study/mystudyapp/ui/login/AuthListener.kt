package com.study.mystudyapp.ui.login

import androidx.lifecycle.LiveData

interface AuthListener {
    fun onStarted()
    fun onResult(loginResponse: LiveData<String>)
    fun onEmptyFiled(message: String)
    //fun onManageComplete(loggedIn: Boolean)

}