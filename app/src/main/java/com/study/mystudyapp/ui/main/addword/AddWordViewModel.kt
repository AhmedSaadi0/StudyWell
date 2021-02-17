package com.study.mystudyapp.ui.main.addword

import androidx.lifecycle.ViewModel
import com.study.mystudyapp.database.repositories.GamesRepository
import com.study.mystudyapp.database.room.games.HanziGame

class AddWordViewModel(private val repo: GamesRepository) : ViewModel() {
    fun insertNewWord(word: HanziGame) = repo.insertNewWord(word)

}