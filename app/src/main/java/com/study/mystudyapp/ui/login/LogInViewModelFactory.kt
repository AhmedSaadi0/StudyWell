package com.study.mystudyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.study.mystudyapp.database.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class LogInViewModelFactory(private val repo: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogInViewModel(repo) as T
    }

}