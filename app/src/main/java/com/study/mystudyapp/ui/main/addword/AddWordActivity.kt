package com.study.mystudyapp.ui.main.addword

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.study.mystudyapp.*
import com.study.mystudyapp.database.room.games.HanziGame
import com.study.mystudyapp.databinding.ActivityAddWordBinding
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

    private var id = ""
    private var _month = ""
    private var _day = ""
    private var _seenCount = 0
    private var _date = MainActivity.date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_word)


        val binding: ActivityAddWordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_word)

        _viewModel = ViewModelProvider(this, factory).get(AddWordViewModel::class.java)

        binding.addWordViewMode = _viewModel

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

        _viewModel?.getWordById(id)?.observeOnce(this, {

            if (it != null) {
                if (it.month != null) {
                    _month = it.month.toString()
                } else {
                    if (MainActivity.date != null) {
                        _month = getYearAndMonth(MainActivity.date!!)
                    }
                }
                _day = it.day
                _seenCount = it.seen_count
            }
        })
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
            data["date"] = getFullDate(MainActivity.date!!)
            data["year"] = getYearAndMonth(MainActivity.date!!)
        } else {
            data["date"] = getFullDate(Date())
            data["year"] = getYearAndMonth(Date())
        }

        if (id.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("users").document(FirebaseAuth.getInstance().uid!!)
                .collection("words").document(id)
                .set(data)


            if (_day.isNotEmpty()) {
                Coroutine.main {
                    _viewModel?.updateWord(
                        HanziGame(
                            id = id,
                            meaning = add_meaning.text.toString(),
                            hanzi = add_symbol.text.toString(),
                            pinyin = add_word.text.toString(),
                            month = _month,
                            day = _day,
                            seen_count = _seenCount,
                            word_length = add_symbol.length()
                        )
                    )
                }
            }

        } else {

            val c: CollectionReference = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().uid!!)
                .collection("words")

            c.document().set(data)

            if (add_symbol.text != null && add_symbol.text?.length!! <= 4) {
                Coroutine.main {
                    if (_date == null) {
                        _date = try {
                            MainActivity.date
                        } catch (ex: NullPointerException) {
                            Date()
                        }
                    }
                    Log.d("TAG", "save: $_date")
                    _viewModel?.insertNewWord(
                        HanziGame(
                            id = c.id,
                            meaning = add_meaning.text.toString(),
                            hanzi = add_symbol.text.toString(),
                            pinyin = add_word.text.toString(),
                            month = getYearAndMonth(_date!!),
                            day = getFullDate(_date!!),
                            seen_count = 0,
                            word_length = add_symbol.length()
                        )
                    )
                }
            }
        }

        finish()
    }

    fun delete(view: View) {
        if (FirebaseAuth.getInstance().uid != null) {

            FirebaseFirestore.getInstance()
                .collection("users").document(FirebaseAuth.getInstance().uid!!)
                .collection("words").document(id)
                .delete()
            Coroutine.main {
                _viewModel?.deleteWord(
                    HanziGame(
                        id = id,
                        meaning = add_meaning.text.toString(),
                        hanzi = add_symbol.text.toString(),
                        pinyin = add_word.text.toString(),
                        month = getYearAndMonth(Date()),
                        day = getFullDate(Date()),
                        seen_count = 0,
                        word_length = add_symbol.length()
                    )
                )
            }

        }

        finish()
    }
}