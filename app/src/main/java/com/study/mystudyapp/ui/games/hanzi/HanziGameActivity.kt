package com.study.mystudyapp.ui.games.hanzi

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.TextView
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
import java.util.*

// it.putExtra("month", getYearToFirebase(MainActivity.date!!))
class HanziGameActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: HanziGameViewModelFactory by instance()
    private var _chosenButtonNumber = 0
    private var _word = ""
    private var _viewModel: HanziGameViewModel? = null
    private var _month = ""
    private var _hanziTextsList: List<TextView>? = null
    private var _pinyinTextsList: List<TextView>? = null
    private var _meaningTextsList: List<TextView>? = null
    private var row: HanziGame? = null
    private var _index = 0
    private var words = mutableListOf<HanziGame>()

    private lateinit var mTTS: TextToSpeech

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

        _hanziTextsList = listOf(
            hanzi1_text,
            hanzi2_text,
            hanzi3_text,
            hanzi4_text
        )
        _meaningTextsList = listOf(
            hanzi1_meaning,
            hanzi2_meaning,
            hanzi3_meaning,
            hanzi4_meaning
        )
        _pinyinTextsList = listOf(
            hanzi1_pinyin,
            hanzi2_pinyin,
            hanzi3_pinyin,
            hanzi4_pinyin
        )

        mTTS = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.SIMPLIFIED_CHINESE
            }
        }

        hideViews()
        setWords()
    }

    private fun setColors() {
        hanzi1.background.setTint(this.getColor(R.color.white))
        hanzi2.background.setTint(this.getColor(R.color.white))
        hanzi3.background.setTint(this.getColor(R.color.white))
        hanzi4.background.setTint(this.getColor(R.color.white))
    }

    private fun setWords() {
        _viewModel?.getOneWord(date = _month)?.observeOnce(this, {

            _word = it.hanzi

            pinyin.text = it.pinyin
            Log.d("Pin", "setWords: ${it.pinyin}")

            meaning.text = it.meaning

            meaning.visibility = View.GONE

            _chosenButtonNumber = rand(0, 3)
            _hanziTextsList?.get(_chosenButtonNumber)?.text = it.hanzi

            fillOtherButtons(_word.length)

            setColors()

            row = it

            mTTS.speak(row?.hanzi, TextToSpeech.QUEUE_FLUSH, null)

        })
    }

    private fun fillOtherButtons(length: Int) {
        words.clear()
        _viewModel?.getRandomWords(_month, length)?.observeOnce(this, {
            _hanziTextsList?.forEachIndexed { index, hanzi_text ->
                if (index != _chosenButtonNumber) {
                    hanzi_text.text = it[index].hanzi

                    _pinyinTextsList?.get(index)?.text = it[index].pinyin
                    _meaningTextsList?.get(index)?.text = it[index].meaning

                    words.add(it[index])
                } else {
                    _index = index
                }
            }
        })
    }

    fun next(view: View) {
        hideViews()
        setWords()
    }


    fun hanzi1(view: View) {
        if (checkWord(hanzi1_text.text.toString())) {
            hanzi1.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi1.background.setTint(this.getColor(R.color.success))
        }
    }

    fun hanzi2(view: View) {
        if (checkWord(hanzi2_text.text.toString())) {
            hanzi2.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi2.background.setTint(this.getColor(R.color.success))
        }
    }

    fun hanzi3(view: View) {
        if (checkWord(hanzi3_text.text.toString())) {
            hanzi3.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi3.background.setTint(this.getColor(R.color.success))
        }
    }

    fun hanzi4(view: View) {
        if (checkWord(hanzi4_text.text.toString())) {
            hanzi4.background.setTint(this.getColor(R.color.danger))
        } else {
            hanzi4.background.setTint(this.getColor(R.color.success))
        }
    }

    private fun checkWord(word: String): Boolean {
        if (word == _word) {

            meaning.visibility = View.VISIBLE

            mTTS.speak(row?.hanzi, TextToSpeech.QUEUE_FLUSH, null)

            if (row?.seen_count != null) {
                row!!.seen_count += 1
                Coroutine.main {
                    _viewModel?.addMoreSeen(row!!)
                }
            }
            showViews()
        }
        return word != _word
    }

    private fun showViews() {
        _pinyinTextsList?.forEachIndexed { index, pinyin ->
            if (_index != index) {
                pinyin.visibility = View.VISIBLE
                _meaningTextsList?.get(index)?.visibility = View.VISIBLE
            }
        }
    }

    private fun hideViews() {
        _pinyinTextsList?.forEachIndexed { index, pinyin ->
            pinyin.visibility = View.GONE
            _meaningTextsList?.get(index)?.visibility = View.GONE
        }
    }

    fun back(view: View) = finish()

}