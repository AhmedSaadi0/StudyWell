package com.study.mystudyapp

import java.text.SimpleDateFormat
import java.util.*

fun getDateToFirebase(date: Date): String {
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)
    return sdf.format(date)
}

fun getYearToFirebase(date: Date): String {
    val sdf = SimpleDateFormat("yyyyMM", Locale.ENGLISH)
    return sdf.format(date)
}


fun getDateToCalender(date: String): Date? {
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)
    return try {
        sdf.parse(date)
    } catch (x: Exception) {
        null
    }

}