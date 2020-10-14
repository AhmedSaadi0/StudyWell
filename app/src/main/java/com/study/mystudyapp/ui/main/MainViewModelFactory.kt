package com.study.mystudyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.study.mystudyapp.database.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repo: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}