package com.study.mystudyapp.ui.main.addword

import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.GamesRepository
import com.study.mystudyapp.database.room.games.HanziGame

class AddWordViewModel(private val repo: GamesRepository) : ViewModel() {
    suspend fun insertNewWord(word: HanziGame) = repo.insertNewWord(word)
    suspend fun updateWord(word: HanziGame) = repo.updateWord(word)
    suspend fun deleteWord(word: HanziGame) = repo.deleteWord(word)
    fun getWordById(id: String) = repo.getWordById(id)

}