package com.dede.dedegame.presentation.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dede.dedegame.R
import com.dede.dedegame.presentation.common.DimensUtil
import com.dede.dedegame.presentation.widget.bottomNavigationView.GridSpacingItemDecoration
import com.dede.dedegame.presentation.widget.bottomNavigationView.CustomBottomNavigationAdapter
import com.dede.dedegame.presentation.widget.bottomNavigationView.CustomBottomNavigationItem
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.jetpack.JetActivity

class HomeView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private lateinit var adapter: HomeAdapter
    private lateinit var vpMain: ViewPager2
    private lateinit var bnvMain: RecyclerView

    override fun onInitView() {
        super.onInitView()
        vpMain = findViewById(R.id.vpHome)
        bnvMain = findViewById(R.id.bottom_nav)
        setupToolbar()
        adapter = HomeAdapter(context as JetActivity<*>)
        vpMain.adapter = adapter
        vpMain.isUserInputEnabled = false

        val items = listOf(
            CustomBottomNavigationItem(R.drawable.ic_tab_home, "Home"),
            CustomBottomNavigationItem(R.drawable.ic_tab_comic, "Story"),
            CustomBottomNavigationItem(R.drawable.ic_tab_game, "Game"),
            CustomBottomNavigationItem(R.drawable.ic_tab_store, "Store"),
        )
        val adapter = CustomBottomNavigationAdapter(context, items)
        bnvMain.layoutManager = GridLayoutManager(context, items.size)
        bnvMain.addItemDecoration(GridSpacingItemDecoration(context, items.size, DimensUtil.dpToPx(10), false))
        adapter.setOnItemSelectedListener(object : CustomBottomNavigationAdapter.OnEventListener{
            override fun OnNavigationItemSelectedListener(pos: Int) {
                when (pos) {
                    0 -> {
                        vpMain.currentItem = 0
                    }
                    1 -> {
                        vpMain.currentItem = 1
                    }
                    2 -> {
                        vpMain.currentItem = 2
                    }
                    3 -> {
                        vpMain.currentItem = 3
                    }
                }
            }
        })
        bnvMain.adapter = adapter
    }

    private fun setupToolbar(){
        val containerBack : View = findViewById(R.id.containerBack)
        val txtStartTitle : TextView = findViewById(R.id.txtStartTitle)
        val txtCenterTitle : TextView = findViewById(R.id.txtCenterTitle)
        containerBack.visibility = View.GONE
        txtStartTitle.text = "Home"
    }

}


