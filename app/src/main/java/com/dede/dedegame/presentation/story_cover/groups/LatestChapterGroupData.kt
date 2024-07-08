package com.dede.dedegame.presentation.story_cover.groups

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.common.DateFormatConverter
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class LatestChapterGroupData(listChapter: List<Chapter>?) :
    GroupData<List<Chapter>>(listChapter) {
    var mPresenter: IPresenter? = null
    var listener: OnClickChapterListener? = null
    private var storyDetail: StoryDetail? = null
    private var isExpanded = false

    companion object{
        const val LIMIT_ITEM_EXPAND = 5
    }

    fun setStoryDetail(story: StoryDetail) {
        storyDetail = story
    }

    fun setExpanded(isExpanded: Boolean) {
        this.isExpanded = isExpanded
    }

    override fun getDataInGroup(position: Int): Any? {
        if (position == 0) {
            return ""
        }
        return try {
            data?.get(position - 1)
        } catch (e: Exception) {
            null
        }
    }

    override fun getCount(): Int {
        val size = data?.size ?: 0
        return when {
            size > LIMIT_ITEM_EXPAND -> if (isExpanded) LIMIT_ITEM_EXPAND + 2 else size + 2
            else -> size + 1
        }
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        if (positionInGroup == 0) {
            return CoverStoryViewType.LATEST_CHAPTER_TITLE
        }

        if (data != null && data.size > LIMIT_ITEM_EXPAND) {
            return when {
                isExpanded && positionInGroup == LIMIT_ITEM_EXPAND + 1 -> CoverStoryViewType.ITEM_EXPANDED
                !isExpanded && (positionInGroup - 1) == data.size -> CoverStoryViewType.ITEM_EXPANDED
                else -> CoverStoryViewType.LATEST_CHAPTER
            }
        }

        return CoverStoryViewType.LATEST_CHAPTER
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        if (viewType == CoverStoryViewType.LATEST_CHAPTER_TITLE) {
            return TitleChapterVH(itemView, this)
        }

        if (viewType == CoverStoryViewType.LATEST_CHAPTER) {
            return LatestChapterVH(itemView, this)
        }

        if (viewType == CoverStoryViewType.ITEM_EXPANDED) {
            return ExpandChapterVH(itemView, this)
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
        if (viewType == CoverStoryViewType.ITEM_EXPANDED) {
            return R.layout.item_child_story_cover_expand_item
        }
        return INVALID_RESOURCE
    }

    private class ExpandChapterVH(
        itemView: View,
        val latestChapterGroupData: LatestChapterGroupData
    ) :
        GroupRclvVH<Chapter, LatestChapterGroupData>(itemView) {

        private val tvExpand by lazy { itemView.findViewById<TextView>(R.id.tvExpand) }
        private val ivExpand by lazy { itemView.findViewById<ImageView>(R.id.ivExpand) }
        override fun onBind(chapter: Chapter?) {
            if (latestChapterGroupData.isExpanded) {
                ivExpand.setImageResource(R.drawable.ic_expand_more_24)
                clickOn(itemView) {
                    latestChapterGroupData.storyDetail?.let { it1 ->
                        latestChapterGroupData.listener?.onClickExpand(
                            it1
                        )
                    }
                }
            } else {
                ivExpand.setImageResource(R.drawable.ic_un_expand_more_24)
                clickOn(itemView) {
                    latestChapterGroupData.storyDetail?.let { it1 ->
                        latestChapterGroupData.listener?.onClickContract(
                            it1
                        )
                    }
                }
            }
        }
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
                val backgroundResource = when {
                    latestChapterGroupData.data != null && latestChapterGroupData.data.size > LIMIT_ITEM_EXPAND && latestChapterGroupData.isExpanded -> {
                        when {
                            it == latestChapterGroupData.data.first() -> R.drawable.bg_top_corner_chapters
                            adapterPosition == LIMIT_ITEM_EXPAND + 2 -> R.drawable.bg_bottom_corner_chapters
                            else -> R.drawable.bg_center_rectangle_chapters
                        }
                    }
                    latestChapterGroupData.data != null && latestChapterGroupData.data.size > 1 -> {
                        when (it) {
                            latestChapterGroupData.data.first() -> R.drawable.bg_top_corner_chapters
                            latestChapterGroupData.data.last() -> R.drawable.bg_bottom_corner_chapters
                            else -> R.drawable.bg_center_rectangle_chapters
                        }
                    }
                    latestChapterGroupData.data != null && latestChapterGroupData.data.size == 1 -> R.drawable.bg_corner_chapters
                    else -> R.color.white
                }

                itemView.setBackgroundResource(backgroundResource)
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
        fun onClickExpand(storyDetail: StoryDetail)
        fun onClickContract(storyDetail: StoryDetail)
    }
}