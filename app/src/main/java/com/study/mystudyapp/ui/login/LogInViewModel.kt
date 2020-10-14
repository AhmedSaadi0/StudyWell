package com.study.mystudyapp.ui.login

import android.view.View
import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.UserRepository

class LogInViewModel(private val repo: UserRepository) : ViewModel() {
    var email: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun onLogInButtonClick(view: View) {
        authListener?.onStarted()

        if (email.isNullOrEmpty()) {
            authListener?.onEmptyFiled("empty email")
            return
        }
        if (password.isNullOrEmpty()) {
            authListener?.onEmptyFiled("empty pass")
            return
        }

        val loginResponse = repo.userLogin(email!!, password!!)

        authListener?.onResult(loginResponse)

    }


}