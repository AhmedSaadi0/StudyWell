package com.study.mystudyapp.database.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.study.mystudyapp.Coroutine
import com.study.mystudyapp.database.room.AppDatabase
import com.study.mystudyapp.database.room.games.HanziGame

class GamesRepository(private val db: AppDatabase) {

    suspend fun setMoreSeen(row: HanziGame) = db.getHanziGameDao().update(row)
    fun getOneWord(month: String) = db.getHanziGameDao().getOneWordByDate(month)


    fun getRandomWords(month: String, length: Int, hanzi: String) =
        db.getHanziGameDao().getRandomWordByDate(month, length, hanzi)

    fun getWords(month: String): LiveData<List<HanziGame>> {
        val data = MutableLiveData<List<HanziGame>>()
        data.value = db.getHanziGameDao().getWordsByDate(month).value
        return data
    }


    fun insertNewWord(word: HanziGame) {
        Coroutine.main {
            db.getHanziGameDao().insert(word)
        }
    }


}