package com.study.mystudyapp.database.room.words

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface WordsDao {

    @Insert
    suspend fun insert(word: WordsTable): Long

    @Query("SELECT * FROM wordstable WHERE category=:category")
    fun getWordsByCategory(category: String): LiveData<List<WordsTable>>

    @Query("SELECT * FROM wordstable")
    fun getAllWords(): LiveData<List<WordsTable>>

}