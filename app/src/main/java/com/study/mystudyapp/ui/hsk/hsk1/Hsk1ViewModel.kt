package com.study.mystudyapp.ui.hsk.hsk1

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.HskRepository

class Hsk1ViewModel(private val repo: HskRepository) : ViewModel() {


    fun check(lifecycleOwner: LifecycleOwner) = repo.check(lifecycleOwner)

    fun getWords(cat: String) = repo.getWords(cat)



}