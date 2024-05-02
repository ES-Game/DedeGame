package com.quangph.dedegame.presentation.home

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseLinearView
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.fragments.HomeFragment
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.jetpack.JetActivity

class HomeAdapter(activity: JetActivity<*>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                return  HomeFragment()
            }

            else -> {
                return HomeFragment()
            }
        }
    }


}


