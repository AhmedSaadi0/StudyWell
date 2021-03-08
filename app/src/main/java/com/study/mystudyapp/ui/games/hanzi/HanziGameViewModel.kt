package com.study.mystudyapp.ui.games.hanzi

import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.GamesRepository
import com.study.mystudyapp.database.room.games.HanziGame

class HanziGameViewModel(private val repo: GamesRepository) : ViewModel() {
    fun getWords(date: String) = repo.getWords(date)
    suspend fun addMoreSeen(row: HanziGame) = repo.setMoreSeen(row)
    fun getOneWordByDate(date: String) = repo.getOneWordByDate(date)
    fun getOneWordByDay(day: String) = repo.getOneWordByDay(day)
    fun getRandomWords(month: String, length: Int, hanzi: String) =
        repo.getRandomWords(month, length, hanzi)
}