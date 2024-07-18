package com.dede.dedegame.presentation.story_cover.groups

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.presentation.common.DateFormatConverter
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class CommentGroupData(comments: List<Comment>?) :
    GroupData<List<Comment>>(comments) {
    var mPresenter: IPresenter? = null
    var listener: OnEventCommentListener? = null
    override fun getDataInGroup(position: Int): Any? {
        if (position == 0) {
            return ""
        }
        return if (data != null && (position - 1) >= data.size) {
            EnumViewType.INPUT_COMMENT
        } else {
            data?.get(position - 1)
        }
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size + 2
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        if (positionInGroup == 0) {
            return CoverStoryViewType.COMMENT_TITTLE
        }
        return if (data != null && positionInGroup > data.size) {
            CoverStoryViewType.COMMENT_INPUT
        } else {
            CoverStoryViewType.LIST_COMMENT
        }
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        if (viewType == CoverStoryViewType.COMMENT_TITTLE) {
            Log.i("onCreateVH", "RANK")
            return TitleChapterVH(itemView, this)
        }

        if (viewType == CoverStoryViewType.LIST_COMMENT) {
            return CommentStoryVH(itemView, this)
        }

        if (viewType == CoverStoryViewType.COMMENT_INPUT) {
            return CommentInputVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == CoverStoryViewType.COMMENT_TITTLE) {
            return R.layout.item_story_cover_lastest_chapter_title
        }
        if (viewType == CoverStoryViewType.LIST_COMMENT) {
            return R.layout.item_story_cover_comment
        }
        if (viewType == CoverStoryViewType.COMMENT_INPUT) {
            return R.layout.item_story_cover_input_comment
        }
        return INVALID_RESOURCE
    }

    private class TitleChapterVH(
        itemView: View,
        val commentGroupData: CommentGroupData
    ) :
        GroupRclvVH<String, CommentGroupData>(itemView) {

        private val txtTitle by lazy { itemView.findViewById<TextView>(R.id.txtSummTitle) }
        override fun onBind(comment: String?) {
            super.onBind(comment)
            txtTitle.text = itemView.context.getString(R.string.story_cover_comment_title)
        }
    }

    private class CommentInputVH(
        itemView: View,
        val commentGroupData: CommentGroupData
    ) :
        GroupRclvVH<Any, CommentGroupData>(itemView) {

        private val edtFeedBack by lazy { itemView.findViewById<EditText>(R.id.edtFeedBack) }
        private val tvDesFeedback by lazy { itemView.findViewById<TextView>(R.id.tvDesFeedback) }
        private val btnSend by lazy { itemView.findViewById<Button>(R.id.btnSend) }
        private val containerUser by lazy { itemView.findViewById<View>(R.id.containerUser) }
        private val tvName by lazy { itemView.findViewById<TextView>(R.id.tvName) }
        override fun onBind(any: Any?) {
            super.onBind(any)
            clickOn(edtFeedBack) {
                if (commentGroupData.listener != null) {
                    commentGroupData.listener?.onClickCommentAction(commentGroupData.data)
                }
            }

            if (DedeSharedPref.getUserInfo()?.authen?.accessToken != null && !DedeSharedPref.getUserInfo()?.authen?.accessToken?.isEmpty()!!) {
                tvDesFeedback.visibility = View.GONE
                containerUser.visibility = View.VISIBLE
                tvName.text = DedeSharedPref.getUserInfo()?.user?.name
            } else {
                tvDesFeedback.visibility = View.VISIBLE
                containerUser.visibility = View.GONE
                tvDesFeedback.text =
                    itemView.context.getString(R.string.story_cover_comment_action_not_login)
            }

            clickOn(btnSend) {
                if (commentGroupData.listener != null) {
                    commentGroupData.listener?.onClickCommentAction(commentGroupData.data)
                }
            }
        }
    }

    private class CommentStoryVH(
        itemView: View,
        val commentGroupData: CommentGroupData
    ) :
        GroupRclvVH<Comment, CommentGroupData>(itemView) {

        private val txtDateTime by lazy { itemView.findViewById<TextView>(R.id.txtDateTime) }
        private val txtName by lazy { itemView.findViewById<TextView>(R.id.txtName) }
        private val txtContent by lazy { itemView.findViewById<TextView>(R.id.txtContent) }
        private val txtLike by lazy { itemView.findViewById<TextView>(R.id.txtLike) }
        private val txtReply by lazy { itemView.findViewById<TextView>(R.id.txtReply) }
        val containerLike by lazy { itemView.findViewById<View>(R.id.containerLike) }
        val tvCountLiked by lazy { itemView.findViewById<TextView>(R.id.tvCountLiked) }
        override fun onBind(comment: Comment?) {
            super.onBind(comment)
            comment?.let { cmt ->
                txtDateTime.text = DateFormatConverter.convertDateFormat(cmt.createdAt)
                txtName.text = cmt.user
                txtContent.text = cmt.comment

                itemView.setPadding(
                    (itemView.context.resources.getDimensionPixelSize(R.dimen.size_icon_40dp) + itemView.context.resources.getDimensionPixelSize(
                        R.dimen.margin_between_part_in_item_10dp
                    )) * cmt.level, 0, 0, 0
                )

                when (cmt.statusLike) {
                    Comment.LikeStatus.LIKED -> {
                        txtLike.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.orange_300
                            )
                        )
                        txtLike.isSelected = true
                    }

                    Comment.LikeStatus.NOT_YET_LIKED -> {
                        txtLike.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.black
                            )
                        )
                        txtLike.isSelected = false
                    }

                    else -> {
                        txtLike.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.black
                            )
                        )
                        txtLike.isSelected = false
                    }
                }

                if (cmt.likes > 0) {
                    containerLike.visibility = View.VISIBLE
                    tvCountLiked.text = cmt.likes.toString()
                } else {
                    containerLike.visibility = View.INVISIBLE
                    tvCountLiked.text = ""
                }

                clickOn(txtLike) {
                    if (commentGroupData.listener != null) {
                        commentGroupData.listener?.onClickLikedComment(cmt)
                    }
                }
                clickOn(txtReply) {
                    if (commentGroupData.listener != null) {
                        commentGroupData.listener?.onClickReplyAction(cmt, commentGroupData.data)
                    }
                }
            }
        }

    }

    fun setOnClickCommentListener(listener: OnEventCommentListener) {
        this.listener = listener
    }

    interface OnEventCommentListener {
        fun onClickCommentAction(comments: List<Comment>)
        fun onClickReplyAction(comment: Comment?, comments: List<Comment>)
        fun onClickLikedComment(comment: Comment?)
    }
}