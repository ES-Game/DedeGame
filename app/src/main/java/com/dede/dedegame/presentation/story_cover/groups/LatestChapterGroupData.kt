package com.dede.dedegame.presentation.story_cover.groups

import android.util.Log
import android.view.View
import android.widget.TextView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.common.DateFormatConverter
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class LatestChapterGroupData(val listChapter: List<Chapter>?) :
    GroupData<List<Chapter>>(listChapter) {
    var mPresenter: IPresenter? = null
    var listener: OnClickChapterListener? = null
    private var storyDetail: StoryDetail? = null

    fun setStoryDetail(story: StoryDetail) {
        storyDetail = story
    }

    override fun getDataInGroup(position: Int): Any? {
        if (position == 0) {
            return ""
        }
        return data.get(position - 1)
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size + 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        if (positionInGroup == 0) {
            return CoverStoryViewType.LATEST_CHAPTER_TITLE
        }
        return CoverStoryViewType.LATEST_CHAPTER
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        if (viewType == CoverStoryViewType.LATEST_CHAPTER_TITLE) {
            Log.i("onCreateVH", "RANK")
            return TitleChapterVH(itemView, this)
        }

        if (viewType == CoverStoryViewType.LATEST_CHAPTER) {
            return LatestChapterVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == CoverStoryViewType.LATEST_CHAPTER_TITLE) {
            return R.layout.item_story_cover_lastest_chapter_title
        }
        if (viewType == CoverStoryViewType.LATEST_CHAPTER) {
            return R.layout.item_child_story_cover_lastest_chapter
        }
        return INVALID_RESOURCE
    }

    private class TitleChapterVH(
        itemView: View,
        val latestChapterGroupData: LatestChapterGroupData
    ) :
        GroupRclvVH<Chapter, LatestChapterGroupData>(itemView) {

    }

    private class LatestChapterVH(
        itemView: View,
        val latestChapterGroupData: LatestChapterGroupData
    ) :
        GroupRclvVH<Chapter, LatestChapterGroupData>(itemView) {

        private val tvNameChapter by lazy { itemView.findViewById<TextView>(R.id.tvNameChapter) }
        private val tvDate by lazy { itemView.findViewById<TextView>(R.id.tvDate) }

        override fun onBind(chapter: Chapter?) {
            super.onBind(chapter)
            chapter?.let {
                tvNameChapter.text = chapter.title
                tvDate.text = DateFormatConverter.convertDateFormat(chapter.publishedAt)
                clickOn(itemView) {
                    latestChapterGroupData.storyDetail?.let { story ->
                        chapter.id?.let { it1 ->
                            latestChapterGroupData.listener?.onClickChapter(
                                story,
                                it1
                            )
                        }
                    }
                }
            }
        }

    }

    interface OnClickChapterListener {
        fun onClickChapter(storyDetail: StoryDetail, chapterId: Int)
    }
}