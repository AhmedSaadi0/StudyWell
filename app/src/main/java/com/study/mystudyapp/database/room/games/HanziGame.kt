package com.study.mystudyapp.database.room.games

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class HanziGame(
    var meaning: String? = null,
    @PrimaryKey()
    var id: String,
    var hanzi: String,
    var pinyin: String? = null,
    var month: String? = null,
    var day: String,
    var seen_count: Int,
    var word_length: Int,
)