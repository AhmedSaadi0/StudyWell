package com.study.mystudyapp.database.room.games

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class HanziGame(
    var meaning: String? = null,
    var hanzi: String? = null,
    var pinyin: String? = null,
    var month: String? = null,
    var seen_count: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}