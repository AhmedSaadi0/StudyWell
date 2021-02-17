package com.study.mystudyapp.database.room.games

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HanziGameDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newWord: HanziGame): Long

    @Query("SELECT * FROM hanzigame WHERE month=:month order by seen_count ASC")
    fun getWordsByDate(month: String): LiveData<List<HanziGame>>

    @Query("SELECT * FROM hanzigame WHERE month=:month order by seen_count ASC LIMIT 1")
    fun getOneWordByDate(month: String): LiveData<HanziGame>

    @Update
    suspend fun update(row: HanziGame)

    @Query("SELECT * FROM hanzigame WHERE month=:month and length(hanzi) <= :length order by RANDOM() LIMIT 4")
    fun getRandomWordByDate(month: String, length: Int): LiveData<List<HanziGame>>

}