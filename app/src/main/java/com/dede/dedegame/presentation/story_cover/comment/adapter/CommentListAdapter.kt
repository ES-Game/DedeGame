package com.dede.dedegame.presentation.story_cover.comment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.presentation.common.DateFormatConverter
import com.dede.dedegame.presentation.story_cover.comment.adapter.CommentListViewType.Companion.ITEM_COMMENT
import com.dede.dedegame.presentation.story_cover.comment.adapter.CommentListViewType.Companion.ITEM_LOAD_MORE

class CommentListAdapter(private var hasLoadMore: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listComment = arrayListOf<Comment>()

    fun getEndPosListComments(): Int {
        return listComment.size
    }

    fun getComments(): List<Comment> {
        return listComment
    }

    fun setComments(list: List<Comment>) {
        val currentSize: Int = listComment.size
        listComment.clear()
        listComment.addAll(list)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, list.size)
    }

    fun addItemsAndNotify(items: List<Comment>) {
        val start: Int = listComment.size
        listComment.addAll(items)
        notifyItemRangeInserted(start, items.size)
    }

    fun setLoadMore(hasLoadMore: Boolean) {
        this.hasLoadMore = hasLoadMore
    }

    override fun getItemCount(): Int {
        val size = if (listComment != null) listComment.size else 0
        Log.i("Size data", size.toString())
        return if (hasLoadMore) {
            size + 1
        } else {
            size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_LOAD_MORE) {
            LoadingVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load_more, parent, false)
            )
        } else {
            CommentGridVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_story_cover_comment, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommentGridVH) {
            val commentDetail = listComment[position]
            commentDetail?.let { cmt ->
                holder.txtDateTime.text = DateFormatConverter.convertDateFormat(cmt.createdAt)
                holder.txtName.text = cmt.user
                holder.txtContent.text = cmt.comment

                holder.itemView.setPadding(
                    (holder.itemView.context.resources.getDimensionPixelSize(R.dimen.size_icon_40dp) + holder.itemView.context.resources.getDimensionPixelSize(
                        R.dimen.margin_between_part_in_item_10dp
                    )) * cmt.level, 0, 0, 0
                )

                when (cmt.statusLike) {
                    Comment.LikeStatus.LIKED -> {
                        holder.txtLike.setTextColor(
                            ContextCompat.getColor(
                                holder.itemView.context,
                                R.color.orange_300
                            )
                        )
                        holder.txtLike.isSelected = true
                    }

                    Comment.LikeStatus.NOT_YET_LIKED -> {
                        holder.txtLike.setTextColor(
                            ContextCompat.getColor(
                                holder.itemView.context,
                                R.color.black
                            )
                        )
                        holder.txtLike.isSelected = false
                    }

                    else -> {
                        holder.txtLike.setTextColor(
                            ContextCompat.getColor(
                                holder.itemView.context,
                                R.color.black
                            )
                        )
                        holder.txtLike.isSelected = false
                    }
                }

                if (cmt.likes > 0) {
                    holder.containerLike.visibility = View.VISIBLE
                    holder.tvCountLiked.text = cmt.likes.toString()
                } else {
                    holder.containerLike.visibility = View.INVISIBLE
                    holder.tvCountLiked.text = ""
                }

                holder.txtLike.setOnClickListener {
                    onClickListener?.onClickLikedComment(cmt)
                }

                holder.txtReply.setOnClickListener {
                    onClickListener?.onClickReplyComment(cmt)
                }

                holder.itemView.setOnClickListener {
                    onClickListener?.onClickItemComment()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == listComment.size) {
            if (hasLoadMore) {
                return ITEM_LOAD_MORE
            } else {
                return ITEM_COMMENT
            }
        } else {
            return ITEM_COMMENT
        }
    }

    inner class LoadingVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    inner class CommentGridVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val txtDateTime by lazy { itemView.findViewById<TextView>(R.id.txtDateTime) }
        val txtName by lazy { itemView.findViewById<TextView>(R.id.txtName) }
        val txtContent by lazy { itemView.findViewById<TextView>(R.id.txtContent) }
        val txtLike by lazy { itemView.findViewById<TextView>(R.id.txtLike) }
        val txtReply by lazy { itemView.findViewById<TextView>(R.id.txtReply) }
        val containerLike by lazy { itemView.findViewById<View>(R.id.containerLike) }
        val tvCountLiked by lazy { itemView.findViewById<TextView>(R.id.tvCountLiked) }

    }

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    interface OnClickListener {
        fun onClickLikedComment(item: Comment)
        fun onClickReplyComment(item: Comment)
        fun onClickItemComment()
    }
}