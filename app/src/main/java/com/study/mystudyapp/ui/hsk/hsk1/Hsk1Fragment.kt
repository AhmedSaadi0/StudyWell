package com.study.mystudyapp.ui.hsk.hsk1

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.study.mystudyapp.R
import com.study.mystudyapp.database.models.WordsModel
import com.study.mystudyapp.database.repositories.HskRepository
import com.study.mystudyapp.databinding.Hsk1FragmentBinding
import com.study.mystudyapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.hsk1_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Hsk1Fragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: Hsk1ViewModelFactory by instance()
    private var word = true
    private var test = false
    private lateinit var adapter: HSK1RVAdapter


    private lateinit var viewModel: Hsk1ViewModel
    private val wordsModel = mutableListOf<WordsModel>()
    private val filterModel = mutableListOf<WordsModel>()

    private val layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: Hsk1FragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.hsk1_fragment, container, false)

        viewModel = ViewModelProvider(this, factory).get(Hsk1ViewModel::class.java)

        binding.hskViewMode = viewModel
        binding.lifecycleOwner = this


        // checking Words Will be deleted when I finish importing data
        viewModel.check(this)



        HskRepository.adding.observe(viewLifecycleOwner, { isEmpty ->
            if (isEmpty) {
                indexing.visibility = View.VISIBLE
                adding_text.visibility = View.VISIBLE

                Handler().postDelayed(
                    {
                        indexing.visibility = View.GONE
                        adding_text.visibility = View.GONE
                        viewModel.getWords("1").observe(viewLifecycleOwner, { list ->

                            if (list.isNotEmpty())
                                list.forEach {
                                    wordsModel.add(
                                        WordsModel(
                                            "",
                                            it.pinyin,
                                            it.word,
                                            type = it.type,
                                            it.meaning,
                                            year = "",
                                            date = ""
                                        )
                                    )
                                    filterData("word")
                                    //adapter.notifyDataSetChanged()
                                }
                        })

                    },
                    10000
                )

            } else {
                indexing.visibility = View.GONE
                adding_text.visibility = View.GONE
                viewModel.getWords("1").observe(viewLifecycleOwner, { list ->

                    if (list.isNotEmpty())
                        list.forEach {
                            wordsModel.add(
                                WordsModel(
                                    "",
                                    it.pinyin,
                                    it.word,
                                    type = it.type,
                                    it.meaning,
                                    year = "",
                                    date = ""
                                )
                            )
                            filterData("word")
                            //adapter.notifyDataSetChanged()
                        }
                })

            }
        })





        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initTopBar()

    }


    private fun initTopBar() {
        hsk1_main_tab_layout.addTab(
            hsk1_main_tab_layout.newTab().setText(getString(R.string.words))
                .setTag("words")
        )

        hsk1_main_tab_layout.addTab(
            hsk1_main_tab_layout.newTab().setText(getString(R.string.sentences))
                .setTag("sentences")
        )

        hsk1_main_tab_layout.addTab(
            hsk1_main_tab_layout.newTab().setText(getString(R.string.test))
                .setTag("test")
        )


        hsk1_main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.tag == "words" && MainActivity.date != null) {
                    layout.spanCount = 2
                    filterData("word")
                } else if (tab?.tag == "sentences" && MainActivity.date != null) {
                    layout.spanCount = 1
                    filterData("sent")
                } else if (tab?.tag == "test" && MainActivity.date != null) {
                    filterData("test")
                }

                // Log.d("TAG", "onTabSelected: Selected${tab?.tag}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

    }

    private fun initRv() {
        adapter = HSK1RVAdapter(requireContext().applicationContext, filterModel)
        hsk1_rv.adapter = adapter
        hsk1_rv.layoutManager = layout
    }

    private fun selector(p: WordsModel): String? = p.pinyin

    private fun filterData(type: String) {
        filterModel.clear()
        adapter.notifyDataSetChanged()

        wordsModel.sortBy { selector(it) }

        wordsModel.forEachIndexed { index, model ->
            model.test = false
            if (model.type == type) {
                filterModel.add(model)
                adapter.notifyDataSetChanged()
                //createTest(date)
            }


        }
    }


}