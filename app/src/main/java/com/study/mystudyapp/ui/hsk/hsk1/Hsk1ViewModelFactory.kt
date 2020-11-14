package com.study.mystudyapp.ui.hsk.hsk1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.study.mystudyapp.database.repositories.HskRepository

@Suppress("UNCHECKED_CAST")
class Hsk1ViewModelFactory(private val repo: HskRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Hsk1ViewModel(repo) as T
    }

}