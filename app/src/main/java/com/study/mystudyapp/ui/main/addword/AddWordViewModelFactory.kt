package com.study.mystudyapp.ui.main.addword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.study.mystudyapp.database.repositories.GamesRepository

@Suppress("UNCHECKED_CAST")
class AddWordViewModelFactory(private val repo: GamesRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddWordViewModel(repo) as T
    }
}