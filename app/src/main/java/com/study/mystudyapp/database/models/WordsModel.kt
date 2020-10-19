package com.study.mystudyapp.database.models

class WordsModel(
    val id: String,
    val pinyin: String?,
    val word: String?,
    val meaning: String?,
    val year: String?,
    val date: String?,
    var test: Boolean = false
)