package com.study.mystudyapp.ui.main.addword

import android.os.Bundle
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

    private var id = "";

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

            _viewModel?.getWordById(id)?.observeOnce(this, {
                Coroutine.main {

                    _viewModel?.updateWord(
                        HanziGame(
                            id = id,
                            meaning = add_meaning.text.toString(),
                            hanzi = add_symbol.text.toString(),
                            pinyin = add_word.text.toString(),
                            month = it.month,
                            day = it.day,
                            seen_count = it.seen_count,
                            word_length = add_symbol.length()
                        )
                    )

                }
            })

        } else {

            val c: CollectionReference = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().uid!!)
                .collection("words")

            c.document().set(data)

            Coroutine.main {


                _viewModel?.insertNewWord(
                    HanziGame(
                        id = c.id,
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

    fun delete(view: View) {
        FirebaseFirestore.getInstance()
            .collection("users").document(FirebaseAuth.getInstance().uid!!)
            .collection("words").document(id)
            .delete()

        finish()
    }
}