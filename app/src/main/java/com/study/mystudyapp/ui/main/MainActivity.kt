package com.study.mystudyapp.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.study.mystudyapp.*
import com.study.mystudyapp.database.models.WordsModel
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val model = mutableListOf<WordsModel>()
    private val viewedModel = mutableListOf<WordsModel>()
    private lateinit var adapter: MainRVAdapter
    private var started = false
    private var word = true
    private var date: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        date = Date()
        initRV()
        initCalender()
        getData(getYearToFirebase(Date()))
        initBottomTabLayout()
    }

    fun add(view: View) {
        startActivity(Intent(this, AddWordActivity::class.java))
    }

    private fun initCalender() {
        main_compat_calender.setFirstDayOfWeek(Calendar.SATURDAY)
        main_compat_calender.setUseThreeLetterAbbreviation(true)


        main_compat_calender.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                date = dateClicked
                if (dateClicked != null)
                    filterData(getDateToFirebase(dateClicked))

            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                if (firstDayOfNewMonth != null)
                    getData(getYearToFirebase(firstDayOfNewMonth))
            }


        })

    }

    private fun filterData(date: String) {
        viewedModel.clear()
        adapter.notifyDataSetChanged()

        model.forEachIndexed { index, model ->
            if (date == model.date) {
                if (word && model.word != null && model.word.length < 6) {
                    viewedModel.add(model)
                    adapter.notifyDataSetChanged()
                } else if (!word && model.word != null && model.word.length >= 6) {
                    viewedModel.add(model)
                    adapter.notifyDataSetChanged()
                }
            }

        }
    }


    private fun initRV() {
        adapter = MainRVAdapter(this, viewedModel)
        main_rv.adapter = adapter
        main_rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }


    private fun getData(year: String) {

        FirebaseFirestore.getInstance()
            .collection("words")
            .whereEqualTo("year", year)
            .addSnapshotListener { value, error ->

                model.clear()
                value?.forEach {
                    model.add(
                        WordsModel(
                            id = it.id,
                            word = it.getString("word"),
                            symbol = it.getString("symbol"),
                            meaning = it.getString("meaning"),
                            date = it.getString("date"),
                            year = it.getString("year")
                        )
                    )

                    adapter.notifyDataSetChanged()

                }

                setEvents()
            }

    }

    private fun setEvents() {
        main_compat_calender.removeAllEvents()
        model.forEach {
            main_compat_calender.addEvent(
                Event(
                    Color.rgb(0, 0, 0),
                    getDateToCalender(it.date.toString())?.time!!
                )
            )
        }

        filterData(getDateToFirebase(Date()))
        started = !started

    }


    private fun initBottomTabLayout() {
        main_tab_layout.addTab(
            main_tab_layout.newTab().setText(getString(R.string.words))
                .setTag("words")
        )
        main_tab_layout.addTab(
            main_tab_layout.newTab().setText(getString(R.string.sentences))
                .setTag("sentences")
        )

        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.tag == "words" && date != null) {
                    word = true
                    filterData(getDateToFirebase(date!!))
                } else if (tab?.tag == "sentences" && date != null) {
                    word = false
                    filterData(getDateToFirebase(date!!))
                }

                Log.d("TAG", "onTabSelected: Selected${tab?.tag}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

}