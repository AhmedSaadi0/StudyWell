package com.study.mystudyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.study.mystudyapp.AddWordActivity
import com.study.mystudyapp.MainRVAdapter
import com.study.mystudyapp.R
import com.study.mystudyapp.database.models.WordsModel
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val model = mutableListOf<WordsModel>()
    private lateinit var adapter: MainRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initRV()
    }

    fun add(view: View) {
        startActivity(Intent(this, AddWordActivity::class.java))
    }


    private fun initRV() {
        adapter = MainRVAdapter(this, model)
        main_rv.adapter = adapter
        main_rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        getData()
    }


    private fun getData() {

        FirebaseFirestore.getInstance()
            .collection("words").addSnapshotListener { value, error ->

                model.clear()
                value?.forEach {
                    model.add(
                        WordsModel(
                            id = it.id,
                            word = it.getString("word"),
                            symbol = it.getString("symbol"),
                            meaning = it.getString("meaning")
                        )
                    )

                    adapter.notifyDataSetChanged()
                }

            }

    }


}