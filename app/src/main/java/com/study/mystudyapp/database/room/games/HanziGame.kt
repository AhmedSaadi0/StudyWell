package com.study.mystudyapp.database.room.games

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class HanziGame(
    var meaning: String? = null,
    @PrimaryKey()
    var hanzi: String,
    var pinyin: String? = null,
    var month: String? = null,
    var seen_count: Int,
)