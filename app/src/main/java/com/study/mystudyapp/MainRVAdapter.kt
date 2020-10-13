package com.study.mystudyapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.mystudyapp.database.models.WordsModel
import kotlinx.android.synthetic.main.word_list_item.view.*

class MainRVAdapter(private val context: Context, private val words: List<WordsModel>) :
    RecyclerView.Adapter<MainRVAdapter.MainRVViewHolder>() {


    class MainRVViewHolder(private val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRVViewHolder {
        return MainRVViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.word_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainRVViewHolder, position: Int) {
        holder.itemView.word.text = words[position].word
        holder.itemView.symbol.text = words[position].symbol
        holder.itemView.meaning.text = words[position].meaning
        if (position % 2 == 0) {
            holder.itemView.con.setPadding(0, 0, 10, 0)
        } else {
            holder.itemView.con.setPadding(10, 0, 0, 0)
        }

        holder.itemView.con.setOnClickListener {
            Intent(context, AddWordActivity::class.java).also {
                it.putExtra("id", words[position].id)
                it.putExtra("word", words[position].word)
                it.putExtra("symbol", words[position].symbol)
                it.putExtra("meaning", words[position].meaning)

                context.startActivity(it)
            }
        }

    }

    override fun getItemCount(): Int = words.size


}