package com.study.mystudyapp.ui.hsk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.study.mystudyapp.R
import kotlinx.android.synthetic.main.activity_main_hsk.*

class MainHskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hsk)

        initViews()
    }


    private fun initViews() {

        val texts: ArrayList<String?> =
            arrayListOf(
                "HSK1",
                "HSK2",
                "HSK3",
                "HSK4",
                "HSK5",
                "HSK6"
            )


        val adapter =
            MainHskRtlViewPagerAdapter(supportFragmentManager)

        main_hsk_rtl_view_pager.adapter = adapter


        main_hsk_tab_layout.setupWithViewPager(main_hsk_rtl_view_pager, false)

        adapter.notifyDataSetChanged()
        adapter.notifyDataSetChanged()


        for (i in 0 until main_hsk_tab_layout.tabCount) {
            main_hsk_tab_layout.getTabAt(i)?.text = texts[i]
        }

    }
}