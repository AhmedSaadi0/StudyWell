package com.study.mystudyapp.database.room.games

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HanziGameDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newWord: HanziGame): Long

    @Query("SELECT * FROM hanzigame WHERE month=:month order by seen_count ASC")
    fun getWordsByDate(month: String): LiveData<List<HanziGame>>

   @Query("SELECT * FROM hanzigame WHERE id=:id")
    fun getOneWordsById(id: String): LiveData<HanziGame>

    @Query("SELECT * FROM hanzigame WHERE month=:month order by seen_count ASC LIMIT 1")
    fun getOneWordByDate(month: String): LiveData<HanziGame>

    @Query("SELECT * FROM hanzigame WHERE day=:day order by seen_count ASC LIMIT 1")
    fun getOneWordByDay(day: String): LiveData<HanziGame>

    @Update
    suspend fun update(row: HanziGame)

    @Delete
    suspend fun delete(row: HanziGame)

    @Query("SELECT * FROM hanzigame WHERE hanzi !=:hanzi and word_length=:length and month=:month order by RANDOM() LIMIT 3")
    fun getRandomWordByDate(month: String, length: Int, hanzi: String): LiveData<List<HanziGame>>

}