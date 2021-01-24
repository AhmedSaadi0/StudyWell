package com.study.mystudyapp.ui.hsk

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.study.mystudyapp.ui.hsk.hsk1.Hsk1Fragment

class MainHskRtlViewPagerAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragments: ArrayList<Fragment> = arrayListOf(
        Hsk1Fragment(),
        //Hsk1Fragment(),
        //Hsk1Fragment(),
        //Hsk1Fragment(),
        //Hsk1Fragment(),
        //Hsk1Fragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }


}

