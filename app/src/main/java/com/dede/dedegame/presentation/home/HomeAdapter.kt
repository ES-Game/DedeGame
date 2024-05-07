package com.dede.dedegame.presentation.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dede.dedegame.presentation.home.fragments.home.HomeFragment
import com.dede.dedegame.presentation.home.fragments.main_game.MainGameFragment
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragment
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

            1 -> {
                return HomeComicsFragment()
            }

            2-> {
                return MainGameFragment()
            }
            else -> {
                return HomeFragment()
            }
        }
    }


}


