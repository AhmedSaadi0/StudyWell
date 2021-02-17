package com.study.mystudyapp.ui.games.hanzi

import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.GamesRepository
import com.study.mystudyapp.database.room.games.HanziGame

class HanziGameViewModel(private val repo: GamesRepository) : ViewModel() {
    fun getWords(date: String) = repo.getWords(date)
    suspend fun addMoreSeen(row: HanziGame) = repo.setMoreSeen(row)
    fun getOneWord(date: String) = repo.getOneWord(date)
    fun getRandomWords(month: String, length: Int) = repo.getRandomWords(month, length)
}