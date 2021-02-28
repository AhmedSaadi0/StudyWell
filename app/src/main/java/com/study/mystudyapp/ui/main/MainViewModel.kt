package com.study.mystudyapp.ui.main

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.models.WordsModel
import com.study.mystudyapp.database.repositories.UserRepository
import com.study.mystudyapp.getYearToFirebase
import com.study.mystudyapp.ui.games.hanzi.HanziGameActivity
import com.study.mystudyapp.ui.hsk.MainHskActivity
import com.study.mystudyapp.ui.login.LogInActivity

class MainViewModel(private val repo: UserRepository) : ViewModel() {

    fun checkHanziGame(
        lifecycleOwner: LifecycleOwner, month: String,
        words: List<WordsModel>
    ) = repo.setGame(
        lifecycleOwner, month, words
    )

    fun openAccount(view: View) {
        Intent(view.context, LogInActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun openHSK(view: View) {
        Intent(view.context, MainHskActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun openHanziGame(view: View) {
        Intent(view.context, HanziGameActivity::class.java).also {
            if (MainActivity.date != null) {
                it.putExtra("month", getYearToFirebase(MainActivity.date!!))
                view.context.startActivity(it)
            } else {
                Toast.makeText(view.context, "صلح التاريخ يا بطل ... ", Toast.LENGTH_SHORT).show()
            }
        }
    }

}