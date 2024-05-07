package com.dede.dedegame.presentation.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.HomeAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.jetpack.JetActivity

class HomeView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private lateinit var adapter: HomeAdapter
    private lateinit var vpMain: ViewPager2
    private lateinit var bnvMain: BottomNavigationView

    override fun onInitView() {
        super.onInitView()
        vpMain = findViewById(R.id.vpHome)
        bnvMain = findViewById(R.id.bottom_nav)
        setupToolbar()
        adapter = HomeAdapter(context as JetActivity<*>)
        vpMain.adapter = adapter
        vpMain.isUserInputEnabled = false

        bnvMain.setOnItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_tab -> {
                    vpMain.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.story_tab -> {
                    vpMain.currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.game_tab -> {
                    vpMain.currentItem = 2
                    return@OnNavigationItemSelectedListener true
                }

                R.id.store_tab -> {
                    vpMain.currentItem = 3
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    private fun setupToolbar(){
        val containerBack : View = findViewById(R.id.containerBack)
        val txtStartTitle : TextView = findViewById(R.id.txtStartTitle)
        val txtCenterTitle : TextView = findViewById(R.id.txtCenterTitle)
        containerBack.visibility = View.GONE
        txtStartTitle.text = "Home"
    }

    fun showHomeFragment() {
        adapter.notifyItemChanged(0)
    }

    fun changeToTabId(tabId: Int) {
        bnvMain.selectedItemId = tabId
    }
}


