package com.study.mystudyapp.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.study.mystudyapp.R
import com.study.mystudyapp.database.models.WordsModel
import com.study.mystudyapp.database.repositories.UserRepository
import com.study.mystudyapp.getFullDate
import com.study.mystudyapp.getYearAndMonth
import com.study.mystudyapp.ui.games.hanzi.HanziGameActivity
import com.study.mystudyapp.ui.hsk.MainHskActivity
import com.study.mystudyapp.ui.login.LogInActivity
import java.util.*

class MainViewModel(private val repo: UserRepository) : ViewModel() {

    fun checkHanziGame(
        lifecycleOwner: LifecycleOwner, date: Date,
        words: List<WordsModel>
    ) = repo.setGame(
        lifecycleOwner, date, words
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

                val a = AlertDialog.Builder(view.context)
                    .setTitle(view.context.getString(R.string.choose_type))

                a.setPositiveButton(view.context.getString(R.string.this_day)) { _, _ ->
                    it.putExtra("day", getFullDate(MainActivity.date!!))
                    it.putExtra("month", getYearAndMonth(MainActivity.date!!))
                    view.context.startActivity(it)
                }

                a.setNegativeButton(view.context.getString(R.string.this_month)) { _, _ ->
                    it.putExtra("month", getYearAndMonth(MainActivity.date!!))
                    view.context.startActivity(it)
                }

                a.show()

            } else {
                Toast.makeText(view.context, "صلح التاريخ يا بطل ... ", Toast.LENGTH_SHORT).show()
            }
        }
    }

}