package com.dede.dedegame.presentation.story_cover.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.widget.decimalRatingBar.DecimalRatingBar
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class TopCoverGroupData(data: StoryDetail?) :
    GroupData<StoryDetail>(data) {
    var mPresenter: IPresenter? = null

    var onClickTopCoverItem: OnClickTopCoverItem? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return CoverStoryViewType.TOP_COVER
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == CoverStoryViewType.TOP_COVER) {
            return TopCoverVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == CoverStoryViewType.TOP_COVER) {
            return R.layout.item_story_cover_top
        }
        return INVALID_RESOURCE
    }

    private class TopCoverVH(itemView: View, val homeTabGroupData: TopCoverGroupData) :
        GroupRclvVH<StoryDetail, TopCoverGroupData>(itemView) {

        private var imvThumbnail: ImageView
        private var imvLike: ImageView
        private var imvBookmark: ImageView
        private var txtStoryName: TextView
        private var txtReadNow: TextView
        private var txtReadLater: TextView
        private var txtFavCount: TextView
        private var txtCountViewer: TextView
        private var txtCountCmt: TextView
        private var txtCmtLabel: TextView
        private var txtStatusLabel: TextView
        private var txtStatus: TextView
        private var ratingBar: DecimalRatingBar
        private var txtRating: TextView
        private var txtCountRating: TextView

        init {
            imvThumbnail = itemView.findViewById(R.id.imvThumbnail)
            imvLike = itemView.findViewById(R.id.imvLike)
            imvBookmark = itemView.findViewById(R.id.imvBookmark)
            txtStoryName = itemView.findViewById(R.id.txtStoryName)
            txtReadNow = itemView.findViewById(R.id.txtReadNow)
            txtReadLater = itemView.findViewById(R.id.txtReadLater)
            txtFavCount = itemView.findViewById(R.id.txtFavCount)
            txtCountViewer = itemView.findViewById(R.id.txtCountViewer)
            txtCountCmt = itemView.findViewById(R.id.txtCountCmt)
            txtCmtLabel = itemView.findViewById(R.id.txtCmtLabel)
            txtStatusLabel = itemView.findViewById(R.id.txtAuthorLabel)
            txtStatus = itemView.findViewById(R.id.txtAuthor)
            ratingBar = itemView.findViewById(R.id.ratingBar)
            txtRating = itemView.findViewById(R.id.txtRating)
            txtCountRating = itemView.findViewById(R.id.txtCountRating)

        }

        override fun onBind(vhData: StoryDetail?) {
            super.onBind(vhData)
            vhData?.let {
                Glide.with(itemView.context).load(it.image).into(imvThumbnail)
                txtStoryName.text = if (it.title.isNullOrEmpty()) "" else it.title
                txtFavCount.text =
                    if (it.likes != null && it.likes != 0) it.likes.toString() else "0"
                txtCountViewer.text =
                    if (it.views != null && it.views != 0) it.views.toString() else "0"
                txtCountCmt.text =
                    if (it.comments != null && it.comments != 0) it.comments.toString() else "0"
                txtCmtLabel.text =
                    if (it.comments != null && it.comments != 0 && it.comments!! > 1) itemView.context.getString(
                        R.string.story_cover_top_comments_label
                    ) else itemView.context.getString(R.string.story_cover_top_comment_label)

                txtReadNow.setOnClickListener {
                    homeTabGroupData.onClickTopCoverItem?.onClickReadNow(vhData)
                }

                when (it.liked) {
                    StoryDetail.InteractionState.INTERACTED -> {
                        imvLike.background = AppCompatResources.getDrawable(imvLike.context, R.drawable.ic_heart)
                    }
                    StoryDetail.InteractionState.NOT_YET_INTERACTED -> {
                        imvLike.background = AppCompatResources.getDrawable(imvLike.context, R.drawable.ic_unlike)
                    }
                    else -> {
                        imvLike.background = AppCompatResources.getDrawable(imvLike.context, R.drawable.ic_unlike)
                    }
                }

                when (it.followed) {
                    StoryDetail.InteractionState.INTERACTED -> {
                        imvBookmark.background = AppCompatResources.getDrawable(imvBookmark.context, R.drawable.ic_bookmark)
                    }
                    StoryDetail.InteractionState.NOT_YET_INTERACTED -> {
                        imvBookmark.background = AppCompatResources.getDrawable(imvBookmark.context, R.drawable.ic_bookmark_disable)
                    }
                    else -> {
                        imvBookmark.background = AppCompatResources.getDrawable(imvBookmark.context, R.drawable.ic_bookmark_disable)
                    }
                }

                if (!it.authors.isNullOrEmpty()) {
                    txtStatusLabel.visibility = View.VISIBLE
                    txtStatus.visibility = View.VISIBLE
                    txtStatus.text = it.authors!![0].name
                } else {
                    txtStatusLabel.visibility = View.INVISIBLE
                    txtStatus.visibility = View.INVISIBLE
                }

                if (it.count != null && it.score != null) {
                    if (it.count!! > 0) {
                        txtRating.text = itemView.context.getString(
                            R.string.chapter_cover_number_rated, it.count!!
                        )
                        txtCountRating.visibility = View.VISIBLE
                        txtCountRating.text =  String.format("%.1f", it.score!!)
                    } else {
                        txtRating.text = itemView.context.getString(
                            R.string.chapter_cover_not_yet_rated
                        )
                        txtCountRating.visibility = View.GONE
                    }
                    ratingBar.rating = it.score!!
                } else {
                    ratingBar.rating = 0F
                    txtCountRating.visibility = View.GONE
                    txtRating.text = itemView.context.getString(
                        R.string.chapter_cover_not_yet_rated
                    )
                }

                imvLike.setOnClickListener {
                    homeTabGroupData.onClickTopCoverItem?.onClickLikeStory(vhData)
                }

                imvBookmark.setOnClickListener {
                    homeTabGroupData.onClickTopCoverItem?.onClickBookmarkStory(vhData)
                }

                ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
                    if (fromUser) {
                        homeTabGroupData.onClickTopCoverItem?.onRatingChanged(
                            rating,
                            it.score!!,
                            it.count!!
                        )
                    }
                }
            }
        }

    }

    interface OnClickTopCoverItem {
        fun onClickLikeStory(story: StoryDetail)
        fun onClickBookmarkStory(story: StoryDetail)
        fun onClickReadNow(item: StoryDetail)
        fun onClickReadLater(item: StoryDetail)
        fun onRatingChanged(newRate: Float, oldRate: Float, count: Int)
    }
}