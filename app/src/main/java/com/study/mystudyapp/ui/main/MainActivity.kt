package com.study.mystudyapp.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.study.mystudyapp.R
import com.study.mystudyapp.database.models.WordsModel
import com.study.mystudyapp.databinding.MainActivityBinding
import com.study.mystudyapp.getDateToCalender
import com.study.mystudyapp.getDateToFirebase
import com.study.mystudyapp.getYearToFirebase
import com.study.mystudyapp.ui.login.LogInActivity
import com.study.mystudyapp.ui.main.addword.AddWordActivity
import kotlinx.android.synthetic.main.main_activity.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()

    private val model = mutableListOf<WordsModel>()
    private val viewedModel = mutableListOf<WordsModel>()
    private lateinit var adapter: MainRVAdapter
    private var started = false
    private var word = true

    companion object {
        var date: Date? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)

        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        binding.viewModel = viewModel


        //moveData()

        date = Date()
        initRV()
        initCalender()
        getData()
        initBottomTabLayout()

        main_toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }


        viewModel.user.observe(this, {
            main_toolbar.title = it.user_email
        })

    }

    fun add(view: View) {
        if (FirebaseAuth.getInstance().uid != null) {
            startActivity(Intent(this, AddWordActivity::class.java))
        } else {
            startActivity(Intent(this, LogInActivity::class.java))
        }
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
                    getData()
            }


        })

    }

    private fun filterData(date: String) {
        viewedModel.clear()
        adapter.notifyDataSetChanged()

        model.forEachIndexed { index, model ->
            if (date == model.date) {
                if (word && model.word != null && !model.word.contains(" ")) {
                    viewedModel.add(model)
                    adapter.notifyDataSetChanged()
                } else if (!word && model.word != null && model.word.contains(" ")) {
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


    private fun getData() {

        val year = getYearToFirebase(date!!)
        if (FirebaseAuth.getInstance().uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users").document(FirebaseAuth.getInstance().uid!!)
                .collection("words")
                .whereEqualTo("year", year)
                .orderBy("word")
                .addSnapshotListener { value, _ ->

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
    }

    private fun moveData() {
        FirebaseFirestore.getInstance()
            .collection("words")
            .addSnapshotListener { value, _ ->
                value?.forEach {
                    val data: MutableMap<String, String> = HashMap()

                    if (it.getString("date") != null && it.getString("year") != null && it.getString(
                            "symbol"
                        ) != null && it.getString("word") != null && it.getString("meaning") != null
                    ) {
                        data["word"] = it.getString("word")!!
                        data["symbol"] = it.getString("symbol")!!
                        data["meaning"] = it.getString("meaning")!!
                        data["date"] = it.getString("date")!!
                        data["year"] = it.getString("year")!!


                        FirebaseFirestore.getInstance()
                            .collection("users").document(FirebaseAuth.getInstance().uid!!)
                            .collection("words").document()
                            .set(data)
                    }
                }
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

        if (date == null) {
            date = Date()
        }
        filterData(getDateToFirebase(date!!))
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