package com.study.mystudyapp.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.study.mystudyapp.R
import com.study.mystudyapp.database.models.WordsModel
import com.study.mystudyapp.toast
import com.study.mystudyapp.ui.main.addword.AddWordActivity
import kotlinx.android.synthetic.main.word_list_item.view.*
import java.util.*

class MainRVAdapter(private val context: Context, private val words: List<WordsModel>) :
    RecyclerView.Adapter<MainRVAdapter.MainRVViewHolder>() {
    private lateinit var mTTS: TextToSpeech

    init {
        mTTS = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.SIMPLIFIED_CHINESE
            }
        }
    }

    class MainRVViewHolder(private val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRVViewHolder {
        return MainRVViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.word_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainRVViewHolder, position: Int) {
        holder.itemView.list_hanzi.text = words[position].hanzi

        if (!words[position].test) {
            holder.itemView.list_pinyin.text = words[position].pinyin
            holder.itemView.meaning.text = words[position].meaning
        } else {
            holder.itemView.list_pinyin.text = ""
            holder.itemView.meaning.text = ""
        }

        if (position % 2 == 0) {
            holder.itemView.con.setPadding(0, 0, 10, 0)
        } else {
            holder.itemView.con.setPadding(10, 0, 0, 0)
        }



        if (words[position].hanzi.isNullOrBlank()) {
            holder.itemView.speak.visibility = View.GONE
        } else {
            holder.itemView.speak.visibility = View.VISIBLE
        }

        holder.itemView.speak.setOnClickListener {
            mTTS.speak(words[position].hanzi, TextToSpeech.QUEUE_FLUSH, null)
        }


        holder.itemView.card.setOnLongClickListener {
            val v = AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.show_pinyin))
                .setMessage(words[position].pinyin)


            v.show()
            true
        }

        holder.itemView.card.setOnClickListener { view ->

            if (FirebaseAuth.getInstance().uid != null) {
                if (!words[position].test) {
                    Intent(context, AddWordActivity::class.java).also {
                        it.putExtra("id", words[position].id)
                        it.putExtra("word", words[position].pinyin)
                        it.putExtra("symbol", words[position].hanzi)
                        it.putExtra("meaning", words[position].meaning)
                        it.putExtra("see_count", words[position].meaning)
                        context.startActivity(it)
                    }
                } else {
                    holder.itemView.list_pinyin.text = ""
                    holder.itemView.meaning.text = ""

                    holder.itemView.list_pinyin.text = words[position].pinyin
                    holder.itemView.meaning.text = words[position].meaning
                }
            } else {
                view.context.toast(view.context.getString(R.string.you_are_not_registered))
            }
        }

    }

    override fun getItemCount(): Int = words.size


}