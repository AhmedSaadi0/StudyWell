package com.study.mystudyapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.study.mystudyapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_add_word.*
import java.util.*
import kotlin.collections.HashMap

class AddWordActivity : AppCompatActivity() {
    private var id = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        if (intent.hasExtra("id"))
            getData()
        else {
            delete.visibility = View.GONE
        }
    }

    private fun getData() {
        id = intent.getStringExtra("id")!!
        add_word.setText(intent.getStringExtra("word"))
        add_symbol.setText(intent.getStringExtra("symbol"))
        add_meaning.setText(intent.getStringExtra("meaning"))
        delete.visibility = View.VISIBLE
    }

    fun save(view: View) {

        val data: MutableMap<String, String> = HashMap()

        if (add_word.text.isNullOrEmpty()) {
            add_word.error = getString(R.string.empty)
        } else {
            data["word"] = add_word.text.toString()

            if (!add_symbol.text.isNullOrEmpty())
                data["symbol"] = add_symbol.text.toString()

            if (!add_meaning.text.isNullOrEmpty())
                data["meaning"] = add_meaning.text.toString()

            if (MainActivity.date != null) {
                data["date"] = getDateToFirebase(MainActivity.date!!)
                data["year"] = getYearToFirebase(MainActivity.date!!)
            } else {
                data["date"] = getDateToFirebase(Date())
                data["year"] = getYearToFirebase(Date())
            }

            if (id.isNotEmpty())
                FirebaseFirestore.getInstance()
                    .collection("words").document(id)
                    .set(data)
            else
                FirebaseFirestore.getInstance()
                    .collection("words").document()
                    .set(data)

            finish()
        }
    }

    fun delete(view: View) {
        FirebaseFirestore.getInstance()
            .collection("words").document(id)
            .delete()

        finish()
    }
}