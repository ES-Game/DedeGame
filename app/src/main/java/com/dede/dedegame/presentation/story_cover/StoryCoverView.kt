package com.dede.dedegame.presentation.story_cover

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.common.CustomItemDecoration
import com.dede.dedegame.presentation.story_cover.groups.LatestChapterGroupData
import com.dede.dedegame.presentation.story_cover.groups.SummaryGroupData
import com.dede.dedegame.presentation.story_cover.groups.TopCoverGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class StoryCoverView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private var rcvInfo: RecyclerView? = null
    private val storyCoverAdapter = GroupRclvAdapter()
    private var topCoverGroupData: TopCoverGroupData = TopCoverGroupData(null)
    private var summaryGroupData: SummaryGroupData = SummaryGroupData(null)
    private val latestChapterGroupData = LatestChapterGroupData(null)
    override fun onInitView() {
        super.onInitView()
        rcvInfo = findViewById<RecyclerView>(R.id.rcvInfo)
        rcvInfo?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcvInfo?.adapter = storyCoverAdapter
        rcvInfo?.addItemDecoration(
            CustomItemDecoration(
                context,
                R.dimen.margin_top_bottom_decorate,
                R.dimen.margin_left_right_decorate
            )
        )
        rcvInfo?.setItemAnimator(null)
        
        setupToolbar()
        storyCoverAdapter.addGroup(topCoverGroupData)
        storyCoverAdapter.addGroup(summaryGroupData)
        storyCoverAdapter.addGroup(latestChapterGroupData)
        topCoverGroupData.onClickTopCoverItem = object : TopCoverGroupData.OnClickTopCoverItem {
            override fun onClickReadNow(item: StoryDetail) {
                mPresenter.executeCommand(GotoChapterCmd(item))
            }

            override fun onClickReadLater(item: StoryDetail) {
            }
        }
        latestChapterGroupData.listener = object : LatestChapterGroupData.OnClickChapterListener {
            override fun onClickChapter(storyDetail: StoryDetail, chapterId: Int) {
                mPresenter.executeCommand(GotoChapterBySelectChapterCmd(storyDetail, chapterId))
            }

            override fun onClickExpand(storyDetail: StoryDetail) {
                latestChapterGroupData.setStoryDetail(storyDetail)
                latestChapterGroupData.setExpanded(false)
                latestChapterGroupData.reset(mChapters)
                latestChapterGroupData.show()
            }

            override fun onClickContract(storyDetail: StoryDetail) {
                latestChapterGroupData.setStoryDetail(storyDetail)
                if (mChapters!!.isNotEmpty() && mChapters?.size!! > 5) {
                    latestChapterGroupData.setExpanded(true)
                }
                latestChapterGroupData.reset(mChapters)
                latestChapterGroupData.show()
            }


        }
    }

    private fun setupToolbar() {
        val containerBack: View = findViewById(R.id.containerBack)
        val txtStartTitle: TextView = findViewById(R.id.txtStartTitle)
        val txtCenterTitle: TextView = findViewById(R.id.txtCenterTitle)
        txtStartTitle.text = "Story Cover"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    fun fillDataToTopGroup(storyDetail: StoryDetail) {
        topCoverGroupData.reset(storyDetail)
        topCoverGroupData.show()
    }

    fun fillDataToSummary(storyDetail: StoryDetail) {
        summaryGroupData.reset(storyDetail)
        summaryGroupData.show()
    }

    private var mChapters: List<Chapter>? = null

    fun fillDataToLatestChapter(storyDetail: StoryDetail, listChapter: List<Chapter>) {
        latestChapterGroupData.setStoryDetail(storyDetail)
        mChapters = listChapter
        if (mChapters?.isNotEmpty()!! && mChapters?.size!! > 5) {
            latestChapterGroupData.setExpanded(true)
        }
        latestChapterGroupData.reset(mChapters)
        latestChapterGroupData.show()
    }

    class GotoChapterCmd(val item: StoryDetail) : ICommand
    class GotoChapterBySelectChapterCmd(val item: StoryDetail, val chapterId: Int) : ICommand
    class OnBackCmd() : ICommand
}


