package com.study.mystudyapp

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

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


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String, time: Int) {
    Toast.makeText(this, message, time).show()
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction(context.getString(R.string.ok)) {
            snackbar.dismiss()
        }
    }.show()
}


fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {

    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })

}

fun rand(start: Int, end: Int): Int {
    require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
    return Random(System.nanoTime()).nextInt(end - start + 1) + start
}
