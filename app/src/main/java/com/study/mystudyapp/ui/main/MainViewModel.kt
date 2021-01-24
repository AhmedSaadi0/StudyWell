package com.study.mystudyapp.ui.main

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.UserRepository
import com.study.mystudyapp.ui.hsk.MainHskActivity
import com.study.mystudyapp.ui.main.addword.AddWordActivity

class MainViewModel(private val repo: UserRepository) : ViewModel() {

    val user = repo.getUser()

    fun openAccount(view: View) {
        Intent(view.context, AddWordActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun openHSK(view: View) {
        Intent(view.context, MainHskActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

}