package com.dede.dedegame.presentation.home.fragments.home_comic

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Category
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.CategoryStoryGroupData
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.HomeComicItemViewType
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.RankStoryGroupData
import com.dede.dedegame.presentation.widget.CatalogueGridSpacingItemDecoration
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class HomeComicsFragmentView(context: Context?, attrs: AttributeSet?) :
    BaseConstraintView(context, attrs) {

    lateinit var rvContent: RecyclerView
    private var btnCategory: AppCompatButton? = null
    private var btnRankList: AppCompatButton? = null
    private var vRankIcons: View? = null

    private val homeContentAdapter = GroupRclvAdapter()
    private val rankGroupData = RankStoryGroupData(null)
    private val layoutManager = GridLayoutManager(context, 3)
    private val layoutManager1 = GridLayoutManager(context, 3)


    override fun onInitView() {
        super.onInitView()
        btnCategory = findViewById(R.id.btn_category)
        btnRankList = findViewById(R.id.btn_rank_list)
        vRankIcons = findViewById(R.id.llIcon)
        rvContent = findViewById<RecyclerView>(R.id.rvContent)

        setupCategoryBtnState(true)
        setupRankListBtnState(false)
        btnCategory?.setOnClickListener {
            setupCategoryBtnState(true)
            setupRankListBtnState(false)
            rvContent.visibility = View.GONE
            mPresenter.executeCommand(OnclickCategoryCmd())
        }


        btnRankList?.setOnClickListener {
            setupCategoryBtnState(false)
            setupRankListBtnState(true)
            rvContent.visibility = View.GONE
            mPresenter.executeCommand(OnclickRankCmd())
        }

        val spacingInPixels = 16
        rvContent.addItemDecoration(CatalogueGridSpacingItemDecoration(spacingInPixels))


        rvContent.adapter = homeContentAdapter

    }

    fun setupCategoryLayout() {

        vRankIcons?.visibility = View.GONE
        rvContent.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = homeContentAdapter.getItemViewType(position)
                return if (itemViewType == HomeComicItemViewType.CATEGORY_TITLE) {
                    3
                } else {
                    1
                }
            }

        }
    }

    fun setupRankLayout() {
        vRankIcons?.visibility = View.VISIBLE
        rvContent.layoutManager = layoutManager1

    }

    fun showStoryCategories(category: Category) {
        if (rankGroupData.isAttached) {
            homeContentAdapter.removeGroup(rankGroupData)
        }
        val categoryGroup = CategoryStoryGroupData(category)
        homeContentAdapter.addGroup(categoryGroup)
        categoryGroup.reset(category)
        categoryGroup.show()
        categoryGroup.onClickStoryItem = object : CategoryStoryGroupData.OnClickStoryItem{
            override fun onClickStoryItem(id: Int) {
                mPresenter.executeCommand(GotoStoryDetailCmd(id))
            }
        }

        rvContent.visibility = View.VISIBLE
    }

    fun showStoryRankList(stories: List<StoryDetail>) {
        homeContentAdapter.clear()
        homeContentAdapter.addGroup(rankGroupData)
        rankGroupData.onClickStoryItem = object : RankStoryGroupData.OnClickStoryItem {
            override fun onClickStoryItem(id: Int) {
                mPresenter.executeCommand(GotoStoryDetailCmd(id))
            }
        }
        rankGroupData.reset(stories)
        rvContent.visibility = View.VISIBLE
    }

    private fun setupCategoryBtnState(isSelected: Boolean) {
        btnCategory?.isSelected = isSelected
        if (isSelected) {
            btnCategory?.background = ContextCompat.getDrawable(context, R.drawable.shape_radius)
        } else {
            btnCategory?.background =
                ContextCompat.getDrawable(context, R.drawable.shape_radius_disable)
        }
    }

    private fun setupRankListBtnState(isSelected: Boolean) {
        btnRankList?.isSelected = isSelected
        if (isSelected) {
            btnRankList?.background = ContextCompat.getDrawable(context, R.drawable.shape_radius)
        } else {
            btnRankList?.background =
                ContextCompat.getDrawable(context, R.drawable.shape_radius_disable)
        }
    }

    class GotoStoryDetailCmd(val id: Int) : ICommand
    class OnclickCategoryCmd : ICommand
    class OnclickRankCmd : ICommand
}


