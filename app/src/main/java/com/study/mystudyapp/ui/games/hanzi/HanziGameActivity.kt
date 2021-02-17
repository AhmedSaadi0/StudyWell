package com.study.mystudyapp.ui.games.hanzi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.study.mystudyapp.*
import com.study.mystudyapp.database.room.games.HanziGame
import com.study.mystudyapp.databinding.ActivityHanziGameBinding
import kotlinx.android.synthetic.main.activity_hanzi_game.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

// it.putExtra("month", getYearToFirebase(MainActivity.date!!))
class HanziGameActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: HanziGameViewModelFactory by instance()
    private var _chosenButtonNumber = 0
    private var _word = ""
    private var _viewModel: HanziGameViewModel? = null
    private var _month = ""
    private var _buttonsList: List<Button>? = null
    private var row: HanziGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hanzi_game)

        val binding: ActivityHanziGameBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_hanzi_game)

        _viewModel = ViewModelProvider(this, factory).get(HanziGameViewModel::class.java)

        binding.viewModel = _viewModel

        if (intent.hasExtra("month")) {
            _month = intent.getStringExtra("month")
        } else {
            finish()
            toast("مابش اكسترا, خاطرك XD")
        }

        _buttonsList = listOf(
            hanzi1,
            hanzi2,
            hanzi3,
            hanzi4
        )

        setWords()
    }

    private fun setColors() {
        hanzi1.background.setTint(this.getColor(R.color.button))
        hanzi2.background.setTint(this.getColor(R.color.button))
        hanzi3.background.setTint(this.getColor(R.color.button))
        hanzi4.background.setTint(this.getColor(R.color.button))
    }

    private fun setWords() {
        _viewModel?.getOneWord(date = _month)?.observeOnce(this, {

            _word = it.hanzi.toString()

            pinyin.text = it.pinyin
            Log.d("Pin", "setWords: ${it.pinyin}")

            meaning.text = it.meaning

            meaning.visibility = View.GONE

            _chosenButtonNumber = rand(0, 3)
            _buttonsList?.get(_chosenButtonNumber)?.text = it.hanzi.toString()


            fillOtherButtons(_word.length)

            setColors()

            row = it

        })
    }

    private fun fillOtherButtons(length: Int) {
        _viewModel?.getRandomWords(_month, length)?.observeOnce(this, {

//            Log.d(TAG, "it: ${it[0]}" )
//            Log.d(TAG, "Buttons: ${_buttonsList?.get(0)}" )

            _buttonsList?.forEachIndexed { index, button ->

                if (index != _chosenButtonNumber) {
                    button.text = it[index].hanzi
                }

            }
        })
    }

    fun next(view: View) {
        setWords()
    }

    fun hanzi1(view: View) {
        if (checkWord(hanzi1.text.toString())) {
            hanzi1.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi1.background.setTint(this.getColor(R.color.success))
        }
    }

    fun hanzi2(view: View) {
        if (checkWord(hanzi2.text.toString())) {
            hanzi2.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi2.background.setTint(this.getColor(R.color.success))
        }
    }

    fun hanzi3(view: View) {
        if (checkWord(hanzi3.text.toString())) {
            hanzi3.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi3.background.setTint(this.getColor(R.color.success))
        }
    }

    fun hanzi4(view: View) {
        if (checkWord(hanzi4.text.toString())) {
            hanzi4.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi4.background.setTint(this.getColor(R.color.success))
        }
    }

    private fun checkWord(word: String): Boolean {
        if (word == _word) {
            meaning.visibility = View.VISIBLE

            if (row?.seen_count != null) {
                row!!.seen_count += 1
                Coroutine.main {
                    _viewModel?.addMoreSeen(row!!)
                }
            }
        }
        return word != _word
    }

}