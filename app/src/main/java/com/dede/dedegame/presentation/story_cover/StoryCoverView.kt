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
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.presentation.common.CustomItemDecoration
import com.dede.dedegame.presentation.story_cover.groups.CommentGroupData
import com.dede.dedegame.presentation.story_cover.groups.EnumListType
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
    private val commentGroupData = CommentGroupData(null)
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

        storyCoverAdapter.addGroup(commentGroupData)

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
                latestChapterGroupData.setListType(EnumListType.GREATER_THAN_LIMIT)
                latestChapterGroupData.reset(mChapters)
                latestChapterGroupData.show()
            }

            override fun onClickContract(storyDetail: StoryDetail) {
                latestChapterGroupData.setListType(EnumListType.EQUAL_LIMIT_WITH_COLLAPSE)
                val listChapter = mChapters
                latestChapterGroupData.reset(listChapter?.subList(0, 5))
                latestChapterGroupData.show()
            }
        }

        commentGroupData.listener = object : CommentGroupData.OnEventCommentListener {
            override fun onClickCommentAction(comments: List<Comment>) {
                mPresenter.executeCommand(ViewCommentCmd(comments))
            }

            override fun onClickReplyAction(comment: Comment?, comments: List<Comment>) {
                mPresenter.executeCommand(OnclickReplyCmd(comment, comments))
            }

            override fun onClickLikedComment(comment: Comment?) {
                mPresenter.executeCommand(OnclickLikedCmd(comment))
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
        if (mChapters?.isNotEmpty()!!) {
            if (mChapters?.size!! > 5) {
                latestChapterGroupData.setListType(EnumListType.EQUAL_LIMIT_WITH_COLLAPSE)
                latestChapterGroupData.reset(listChapter.subList(0, 5))
            } else if (mChapters?.size!! == 5) {
                latestChapterGroupData.setListType(EnumListType.EQUAL_LIMIT_NO_COLLAPSE)
                latestChapterGroupData.reset(listChapter)
            } else {
                latestChapterGroupData.setListType(EnumListType.LESS_THAN_LIMIT)
                latestChapterGroupData.reset(listChapter)
            }
            latestChapterGroupData.show()
        }

    }

    fun fillDataToComment(listComment: List<Comment>) {
        commentGroupData.reset(listComment)
        commentGroupData.show()
    }

    class GotoChapterCmd(val item: StoryDetail) : ICommand
    class GotoChapterBySelectChapterCmd(val item: StoryDetail, val chapterId: Int) : ICommand
    class OnclickLikedCmd(val comment: Comment?) : ICommand
    class OnclickReplyCmd(val comment: Comment?, val comments: List<Comment>) : ICommand
    class ViewCommentCmd(val comments: List<Comment>) : ICommand
    class OnBackCmd() : ICommand
}


