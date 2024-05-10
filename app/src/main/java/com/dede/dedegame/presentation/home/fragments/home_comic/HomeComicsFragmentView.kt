package com.dede.dedegame.presentation.home.fragments.home_comic

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quangph.base.mvp.ICommand
import com.dede.dedegame.R
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.CategoriesStoryGroupData
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.CategoryStoryGroupData
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.CategoryTitleGroupData
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.HomeComicItemViewType
import com.dede.dedegame.presentation.home.fragments.home_comic.groups.RankStoryGroupData
import com.dede.dedegame.presentation.widget.CatalogueGridSpacingItemDecoration
import com.google.gson.Gson
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter
import com.quangph.dedegame.domain.model.Category
import com.quangph.dedegame.domain.model.StoryDetail

class HomeComicsFragmentView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    lateinit var rvContent: RecyclerView
    private var btnCategory: Button? = null
    private var btnRankList: Button? = null
    private var vRankIcons: View? = null

    private val homeContentAdapter = GroupRclvAdapter()
    private val rankGroupData = RankStoryGroupData(null)
    private val layoutManager = GridLayoutManager(context, 3)
    private val layoutManager1 = GridLayoutManager(context, 3)


    override fun onInitView() {
        super.onInitView()
        btnCategory = findViewById(R.id.btn_category)
        setupCategoryBtnState(true)
        setupRankListBtnState(false)
        btnCategory?.setOnClickListener {
            setupCategoryBtnState(true)
            setupRankListBtnState(false)
            rvContent.visibility = View.GONE
            mPresenter.executeCommand(OnclickCategoryCmd())
        }

        btnRankList = findViewById(R.id.btn_rank_list)
        btnRankList?.setOnClickListener {
            setupCategoryBtnState(false)
            setupRankListBtnState(true)
            rvContent.visibility = View.GONE
            mPresenter.executeCommand(OnclickRankCmd())
        }


        vRankIcons = findViewById(R.id.llIcon)

        rvContent = findViewById<RecyclerView>(R.id.rvContent)

        val spacingInPixels = 16
        rvContent.addItemDecoration(CatalogueGridSpacingItemDecoration(spacingInPixels));


        rvContent!!.adapter = homeContentAdapter

    }

    fun setupCategoryLayout() {

        vRankIcons?.visibility = View.GONE
        rvContent!!.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                var vh = rvContent.findViewHolderForAdapterPosition(position)
                val itemViewType = homeContentAdapter.getItemViewType(position)
                if (itemViewType == HomeComicItemViewType.CATEGORY_TITLE) {
                    return 3
                } else {
                    return 1
                }
            }

        }
    }

    fun setupRankLayout() {
        vRankIcons?.visibility = View.VISIBLE
        rvContent!!.layoutManager = layoutManager1

    }

    fun showStoryCategories(stories: List<Category>) {
        homeContentAdapter.clear()
        for ((index, cate) in stories.withIndex()) {
            val categoryGroup = CategoryStoryGroupData(cate)
            homeContentAdapter.addRawGroupAtIndex(index, categoryGroup)
            categoryGroup.show()
            Log.i("Current cate index: ", index.toString())
            Log.i("Current adapter index: ", categoryGroup.adapterPosition.toString())

        }
        rvContent.visibility = View.VISIBLE
    }

    fun showStoryRankList(stories: List<StoryDetail>) {
        homeContentAdapter.clear()
        homeContentAdapter.addGroup(rankGroupData)
        rankGroupData.reset(stories)
        rvContent.visibility = View.VISIBLE
    }

    private fun setupCategoryBtnState(isSelected: Boolean) {
        btnCategory?.isSelected = isSelected
    }

    private fun setupRankListBtnState(isSelected: Boolean) {
        btnRankList?.isSelected = isSelected
    }

    class GotoStoryDetailCmd(val item: StoryDetail) : ICommand
    class OnclickCategoryCmd: ICommand
    class OnclickRankCmd: ICommand
}


