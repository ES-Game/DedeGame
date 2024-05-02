package com.quangph.dedegame.presentation.home

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import androidx.viewpager2.widget.ViewPager2
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseLinearView
import com.dede.dedegame.R
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

        adapter = HomeAdapter(context as JetActivity<*>)
        vpMain.adapter = adapter
        vpMain.isUserInputEnabled = false

        bnvMain.setOnItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_tab -> {
                    vpMain.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.store_tab -> {
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

    fun showHomeFragment() {
        adapter.notifyItemChanged(0)
    }

    fun changeToTabId(tabId: Int) {
        bnvMain.selectedItemId = tabId
    }

    class SubmitBookCmd(
        val bookTitle: String, val bookAuthor: String, val bookPublisher: String, val bookDes: String
    ) : ICommand
}


