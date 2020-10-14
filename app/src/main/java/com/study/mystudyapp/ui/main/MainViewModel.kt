package com.study.mystudyapp.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.UserRepository

class MainViewModel(private val repo: UserRepository) : ViewModel() {

    val user = repo.getUser()


}