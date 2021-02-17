package com.study.mystudyapp.ui.games.hanzi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.study.mystudyapp.database.repositories.GamesRepository
import com.study.mystudyapp.database.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class HanziGameViewModelFactory(private val repo: GamesRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HanziGameViewModel(repo) as T
    }
}