package com.study.mystudyapp.database.room.words

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordsTable(
    var word: String,
    var pinyin: String? = null,
    var meaning: String,
    var type: String,
    var category: String
) {
    @PrimaryKey(autoGenerate = true)
    var word_id: Int = 0
}