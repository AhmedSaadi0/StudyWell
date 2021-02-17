package com.study.mystudyapp.ui.main.addword

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.study.mystudyapp.R
import com.study.mystudyapp.database.room.games.HanziGame
import com.study.mystudyapp.databinding.ActivityAddWordBinding
import com.study.mystudyapp.getDateToFirebase
import com.study.mystudyapp.getYearToFirebase
import com.study.mystudyapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_add_word.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.HashMap

class AddWordActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: AddWordViewModelFactory by instance()
    private var _viewModel: AddWordViewModel? = null

    private var id = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)


//        val binding: ActivityAddWordBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_add_word)
//
//        _viewModel = ViewModelProvider(this, factory).get(AddWordViewModel::class.java)
//
//        binding.addWordViewMode = _viewModel

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
                .collection("users").document(FirebaseAuth.getInstance().uid!!)
                .collection("words").document(id)
                .set(data)
        else
            FirebaseFirestore.getInstance()
                .collection("users").document(FirebaseAuth.getInstance().uid!!)
                .collection("words").document()
                .set(data)

        _viewModel?.insertNewWord(
            HanziGame(
                meaning = add_meaning.text.toString(),
                hanzi = add_symbol.text.toString(),
                pinyin = add_word.text.toString(),
                month = getDateToFirebase(Date()),
                seen_count = 0
            )
        )

        finish()

    }

    fun delete(view: View) {
        FirebaseFirestore.getInstance()
            .collection("users").document(FirebaseAuth.getInstance().uid!!)
            .collection("words").document(id)
            .delete()

        finish()
    }
}