package com.study.mystudyapp.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
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
    private var test = false
    private var _viewModel: MainViewModel? = null

    companion object {
        var date: Date? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)

        _viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        binding.viewModel = _viewModel


        //moveData()

        date = Date()
        initRV()
        initCalender()
        //getData()
        initBottomTabLayout()


        /*
        main_toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }


        viewModel.user.observe(this, androidx.lifecycle.Observer {
            if (it != null && it.user_email != null) {
                main_toolbar.title = it.user_email
            }
        })
*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        getData()

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
                date = firstDayOfNewMonth
                if (firstDayOfNewMonth != null)
                    getData()
            }

        })

    }

    private fun selector(p: WordsModel): String? = p.hanzi

    private fun filterData(date: String) {
        viewedModel.clear()
        adapter.notifyDataSetChanged()

        model.sortBy { selector(it) }

        model.forEachIndexed { index, model ->
            if (date == model.date) {
                model.test = false
                // true & false -> word
                // false & false -> sentence
                // false & true -> test
                if (word && !test && model.pinyin != null && !model.pinyin.contains(" ")) {
                    viewedModel.add(model)
                    adapter.notifyDataSetChanged()
                } else if (!word && !test && model.pinyin != null && model.pinyin.contains(" ")) {
                    viewedModel.add(model)
                    adapter.notifyDataSetChanged()
                } else if (!word && test) {
                    createTest(date)
                }
            }

        }
    }

    private fun createTest(date: String) {
        viewedModel.clear()
        adapter.notifyDataSetChanged()

        model.shuffle()

        model.forEachIndexed { _, model ->

            if (date == model.date) {
                if (model.pinyin != null) {

                    model.test = true
                    Log.d("TAG", model.test.toString())
                    viewedModel.add(model)
                    adapter.notifyDataSetChanged()

                }
            }

        }
    }


    private fun initRV() {
        adapter = MainRVAdapter(this, viewedModel)
        main_rv.adapter = adapter
        main_rv.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun checking(year: String) {
        val dialog = AlertDialog.Builder(this).create()
        dialog.setTitle("Setting up ... please wait")
        dialog.setView(ProgressBar(this))

        _viewModel?.checkHanziGame(this, year, model)?.observe(this, {
            if (!it) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
            Log.d("Value", it.toString())
        })
    }


    private fun getData() {

        val year = getYearToFirebase(date!!)
        if (FirebaseAuth.getInstance().uid == null) {
            FirebaseFirestore.getInstance()
                .collection("users").document("JSY3LPJlI5h7DuYWEhddYzX3DDm2")
                .collection("words")
                .whereEqualTo("year", year)
                .orderBy("word")
                .addSnapshotListener { value, _ ->

                    model.clear()
                    value?.forEach {
                        model.add(
                            WordsModel(
                                id = it.id,
                                pinyin = it.getString("word"),
                                hanzi = it.getString("symbol"),
                                meaning = it.getString("meaning"),
                                date = it.getString("date"),
                                year = it.getString("year"),
                                type = ""
                            )
                        )

                        adapter.notifyDataSetChanged()

                    }

                    checking(year)
                    setEvents()
                }
        } else if (FirebaseAuth.getInstance().uid != null) {
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
                                pinyin = it.getString("word"),
                                hanzi = it.getString("symbol"),
                                meaning = it.getString("meaning"),
                                date = it.getString("date"),
                                year = it.getString("year"),
                                type = ""
                            )
                        )
                        adapter.notifyDataSetChanged()
                    }

                    checking(year)
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
        main_tab_layout.addTab(
            main_tab_layout.newTab().setText(getString(R.string.test))
                .setTag("test")
        )

        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.tag == "words" && date != null) {
                    word = true
                    test = false
                    filterData(getDateToFirebase(date!!))
                } else if (tab?.tag == "sentences" && date != null) {
                    word = false
                    test = false
                    filterData(getDateToFirebase(date!!))
                } else if (tab?.tag == "test" && date != null) {

                    word = false
                    test = true
                    createTest(getDateToFirebase(date!!))
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