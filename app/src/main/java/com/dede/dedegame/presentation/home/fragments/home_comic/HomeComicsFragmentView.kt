package com.dede.dedegame.presentation.home.fragments.home_comic

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quangph.base.mvp.ICommand
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.fragments.home.groups.HomeTabGroupData
import com.dede.dedegame.presentation.home.fragments.home.groups.ListStoryGroupData
import com.dede.dedegame.presentation.home.fragments.main_game.groups.TopBannerGroupData
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter
import com.quangph.dedegame.domain.model.StoryDetail

class HomeComicsFragmentView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private var rvContent: RecyclerView? = null
    private var btnCategory: Button? = null
    private var btnRankList: Button? = null

    private val homeContentAdapter = GroupRclvAdapter()
    private val topBannerGroupData = TopBannerGroupData(null)
    private val homeTabGroupData = HomeTabGroupData(null)
    private val listStoryGroupData = ListStoryGroupData(null)


    override fun onInitView() {
        super.onInitView()
        btnCategory = findViewById(R.id.btn_category)
        setupCategoryBtnState(true)
        btnCategory?.setOnClickListener {
            setupCategoryBtnState(false)
            setupRankListBtnState(true)
        }

        btnRankList = findViewById(R.id.btn_rank_list)
        btnRankList?.setOnClickListener {
            setupCategoryBtnState(true)
            setupRankListBtnState(false)
        }

        setupRankListBtnState(false)

        rvContent = findViewById<RecyclerView>(R.id.rvContent)

        rvContent!!.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        rvContent!!.adapter = homeContentAdapter

    }



    fun showListStory(data: List<StoryDetail>) {
        listStoryGroupData.reset(data)
        listStoryGroupData.show()
    }

    private fun setupCategoryBtnState(isSelected: Boolean) {
        btnCategory?.isSelected = isSelected
    }

    private fun setupRankListBtnState(isSelected: Boolean) {
        btnRankList?.isSelected = isSelected
    }

    class GotoStoryDetailCmd(val item: StoryDetail) : ICommand
}


