package com.dede.dedegame.presentation.widget.dialog

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.AppConfig
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.usecase.GetCommentByStoryId
import com.dede.dedegame.domain.usecase.LikeCommentStory
import com.dede.dedegame.domain.usecase.RefreshToken
import com.dede.dedegame.domain.usecase.ReplyComment
import com.dede.dedegame.domain.usecase.SendCommentToStory
import com.dede.dedegame.domain.usecase.UnLikeCommentStory
import com.dede.dedegame.presentation.common.CustomItemDecoration
import com.dede.dedegame.presentation.login.LoginActivity
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.dede.dedegame.presentation.story_cover.comment.adapter.CommentListAdapter
import com.dede.dedegame.presentation.widget.EndlessRecyclerViewScrollListener
import com.dede.dedegame.repo.network.APIActionException
import com.dede.dedegame.repo.user.exception.LogoutException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException


class CommentDetailDialog : BottomSheetDialogFragment() {
    lateinit var rootView: ViewGroup
    lateinit var editText: EditText
    lateinit var rvComments: RecyclerView
    lateinit var imvSend: ImageView
    lateinit var emptyView: View
    lateinit var replyView: View
    lateinit var tvReplyEveryOne: TextView
    lateinit var tvCancelEveryOne: TextView
    lateinit var containerBack: View

    private var commentListAdapter = CommentListAdapter()
    private val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    private var comments: List<Comment> = arrayListOf()
    private var mComment: Comment? = null
    private var mStoryId: Int? = null
    private var isLoading = false
    private var currentPage = 1
    private var mLastPage = 1
    private var hasUpdate = false
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    companion object {
        private const val KEY_STORY_ID = "key_story_id"
        private const val KEY_COMMENT_ITEM = "key_comment_item"
        private const val KEY_COMMENT_LIST = "key_comment_list"

        fun newInstance(
            storyId: Int,
            comment: Comment?,
            comments: List<Comment>
        ): CommentDetailDialog {
            val frag =
                CommentDetailDialog()
            val args = Bundle()
            args.putInt(KEY_STORY_ID, storyId)
            args.putString(KEY_COMMENT_LIST, Gson().toJson(comments))
            comment?.let {
                args.putString(KEY_COMMENT_ITEM, Gson().toJson(comment))
            }
            frag.setArguments(args)
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CommonDialog)
        arguments?.let {
            val listType = object : TypeToken<List<Comment>>() {}.type
            comments = Gson().fromJson(it.getString(KEY_COMMENT_LIST), listType)
            mComment = Gson().fromJson(it.getString(KEY_COMMENT_ITEM), Comment::class.java)
            mStoryId = it.getInt(KEY_STORY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_comment_detail, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.color.color_background_common_dialog)
        dialog?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_background_common_dialog)
        rootView = view.findViewById(R.id.root_layout)
        emptyView = view.findViewById(R.id.emptyView)
        editText = view.findViewById(R.id.editText)
        rvComments = view.findViewById(R.id.rvComments)
        imvSend = view.findViewById(R.id.imvSend)
        replyView = view.findViewById(R.id.replyView)
        tvReplyEveryOne = view.findViewById(R.id.tvReplyEveryOne)
        tvCancelEveryOne = view.findViewById(R.id.tvCancelEveryOne)
        containerBack = view.findViewById(R.id.containerBack)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        val behavior = BottomSheetBehavior.from(bottomSheet as View)
        behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        initView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            val behavior = BottomSheetBehavior.from(bottomSheet as View)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun initView() {
        containerBack.setOnClickListener {
            dismiss()
            onEventDialogListener?.onRefreshData(hasUpdate)
        }
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            editText.post {
                val inputMethodManager = activity?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.showSoftInput(
                    editText,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
        }
        editText.requestFocus()
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.toString()?.replace(" ", "")?.length!! > 0) {
                    setStateSendButton(true)
                } else {
                    setStateSendButton(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        if (mComment != null) {
            val userTag = "${mComment?.user}" + " "
            editText.setText(userTag)
            editText.setSelection(userTag.length)
            replyView.visibility = View.VISIBLE
            setStateReplyView()
            setStateSendButton(true)
        } else {
            replyView.visibility = View.GONE
            setStateSendButton(false)
        }

        tvCancelEveryOne.setOnClickListener {
            mComment = null
            editText.setText("")
            tvReplyEveryOne.text = ""
            replyView.visibility = View.GONE
            setStateSendButton(false)
        }

        rootView.setOnClickListener {
            hideKeyboard()
        }

        if (comments.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
        rvComments.setItemAnimator(null)
        rvComments.adapter = commentListAdapter
        rvComments.layoutManager = layoutManager
        rvComments.addItemDecoration(
            CustomItemDecoration(
                requireContext(),
                R.dimen.margin_top_bottom_decorate,
                R.dimen.margin_left_right_decorate
            )
        )
        commentListAdapter.setOnClickListener(object : CommentListAdapter.OnClickListener {
            override fun onClickLikedComment(item: Comment) {
                when (item.statusLike) {
                    Comment.LikeStatus.LIKED -> {
                        unLikeCommentStory(mStoryId!!, item.id!!, commentListAdapter.getComments())
                    }

                    Comment.LikeStatus.NOT_YET_LIKED -> {
                        likeCommentStory(mStoryId!!, item.id!!, commentListAdapter.getComments())
                    }

                    else -> {
                        refreshToken(
                            DedeSharedPref.getUserInfo()?.authen?.refreshToken!!,
                            AppConfig.clientId,
                            AppConfig.clientSecret
                        ) {
                            currentPage = 0
                            hasUpdate = true
                            mLastPage = 1
                            commentListAdapter.setComments(emptyList())
                            scrollListener.resetState()
                            loadMoreItems()
                        }
                    }
                }
            }

            override fun onClickReplyComment(item: Comment) {
                if (mComment == null) {
                    mComment = item
                    val userTag = "${mComment?.user}" + " "
                    editText.setText(userTag)
                    editText.setSelection(userTag.length)
                    setStateReplyView()
                    replyView.visibility = View.VISIBLE
                    setStateSendButton(true)
                } else {
                    mComment = item
                    val userTag = "${mComment?.user}" + " "
                    editText.setText(userTag)
                    editText.setSelection(userTag.length)
                    setStateReplyView()
                    replyView.visibility = View.VISIBLE
                    setStateSendButton(true)
                }
            }

            override fun onClickItemComment() {
                hideKeyboard()
            }
        })

        imvSend.setOnClickListener {
            if (mComment != null) {
                val rv = ReplyComment.RV().apply {
                    this.storyId = mStoryId!!
                    this.comment = editText.text.toString()
                    this.parentId = mComment?.id!!
                }
                (activity as StoryCoverActivity).mActionManager.executeAction(
                    ReplyComment(),
                    rv,
                    object : Action.SimpleActionCallback<Comment>() {
                        override fun onSuccess(responseValue: Comment?) {
                            super.onSuccess(responseValue)
                            responseValue?.let {
                                editText.setText("")
                                currentPage = 0
                                hasUpdate = true
                                mLastPage = 1
                                commentListAdapter.setComments(emptyList())
                                scrollListener.resetState()
                                loadMoreItems()
                            }
                        }

                        override fun onError(e: ActionException) {
                            super.onError(e)
                            if (e.cause is LogoutException) {
                                logOut()
                            } else {
                                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            } else {
                val rv = SendCommentToStory.RV().apply {
                    this.storyId = mStoryId!!
                    this.comment = editText.text.toString()
                }
                (activity as StoryCoverActivity).mActionManager.executeAction(
                    SendCommentToStory(),
                    rv,
                    object : Action.SimpleActionCallback<Comment>() {
                        override fun onSuccess(responseValue: Comment?) {
                            super.onSuccess(responseValue)
                            responseValue?.let {
                                editText.setText("")
                                currentPage = 0
                                hasUpdate = true
                                mLastPage = 1
                                commentListAdapter.setComments(emptyList())
                                scrollListener.resetState()
                                loadMoreItems()
                            }
                        }

                        override fun onError(e: ActionException) {
                            super.onError(e)
                            if (e.cause is LogoutException) {
                                logOut()
                            } else {
                                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }
        }
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (currentPage <= mLastPage && !isLoading) {
                    loadMoreItems()
                }
            }
        }
        rvComments.addOnScrollListener(scrollListener)
        commentListAdapter.addItems(comments)
    }

    private fun loadMoreItems() {
        isLoading = true
        commentListAdapter.addLoadingFooter()
        currentPage += 1
        if (activity is StoryCoverActivity) {
            val rv = GetCommentByStoryId.RV().apply {
                this.storyId = mStoryId!!
                this.page = currentPage
            }
            (activity as StoryCoverActivity).mActionManager.executeAction(
                GetCommentByStoryId(),
                rv,
                object : Action.SimpleActionCallback<DataPage<Comment>>() {
                    override fun onSuccess(responseValue: DataPage<Comment>?) {
                        super.onSuccess(responseValue)
                        commentListAdapter.removeLoadingFooter()
                        if (comments.isEmpty()) {
                            emptyView.visibility = View.GONE
                        }
                        responseValue?.dataList?.let {
                            commentListAdapter.addItems(it)
                            mLastPage = responseValue.lastPage
                            isLoading = false
                        }
                    }

                    override fun onError(e: ActionException) {
                        super.onError(e)
                        commentListAdapter.removeLoadingFooter()
                        isLoading = false
                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        dialog?.currentFocus.let { view ->
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun setStateSendButton(isEnabled: Boolean) {
        imvSend.isEnabled = isEnabled
        val colorResId: Int =
            if (isEnabled) R.color.orange_300 else R.color.color_app_common_hint_edt
        val colorStateList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorResId))
        ImageViewCompat.setImageTintList(imvSend, colorStateList)
    }

    private fun setStateReplyView() {
        val rawText = getString(R.string.all_comment_screen_replying)
        val formattedText = java.lang.String.format(rawText, mComment?.user)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvReplyEveryOne.text = Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            tvReplyEveryOne.text = Html.fromHtml(formattedText)
        }
    }

    private fun likeCommentStory(id: Int, commentId: Int, listComment: List<Comment>) {
        val rv = LikeCommentStory.RV().apply {
            this.storyId = id
            this.commentId = commentId
        }

        (activity as StoryCoverActivity).mActionManager.executeAction(
            LikeCommentStory(),
            rv,
            object : Action.SimpleActionCallback<Int>() {
                override fun onSuccess(responseValue: Int?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        listComment.find { cmt -> cmt.id == commentId }?.let {
                            it.statusLike = Comment.LikeStatus.LIKED
                            it.likes += 1
                            commentListAdapter.notifyItemChanged(listComment.indexOf(it), 1)
                            hasUpdate = true
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    if (e.cause is LogoutException) {
                        logOut()
                    } else {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun unLikeCommentStory(id: Int, commentId: Int, listComment: List<Comment>) {
        val rv = UnLikeCommentStory.RV().apply {
            this.storyId = id
            this.commentId = commentId
        }

        (activity as StoryCoverActivity).mActionManager.executeAction(
            UnLikeCommentStory(),
            rv,
            object : Action.SimpleActionCallback<Int>() {
                override fun onSuccess(responseValue: Int?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        listComment.find { cmt -> cmt.id == commentId }?.let {
                            it.statusLike = Comment.LikeStatus.NOT_YET_LIKED
                            it.likes -= 1
                            commentListAdapter.notifyItemChanged(listComment.indexOf(it), 1)
                            hasUpdate = true
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    if (e.cause is LogoutException) {
                        logOut()
                    } else {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun refreshToken(
        token: String,
        clientId: Int,
        clientSecret: String,
        callback: () -> Unit
    ) {
        (activity as StoryCoverActivity).showLoading()
        val rv = RefreshToken.RV().apply {
            this.token = token
            this.clientId = clientId
            this.clientSecret = clientSecret
        }

        (activity as StoryCoverActivity).mActionManager.executeAction(
            RefreshToken(),
            rv,
            object : Action.SimpleActionCallback<UserInfo>() {
                override fun onSuccess(responseValue: UserInfo?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        callback()
                    } ?: run {
                        (activity as StoryCoverActivity).hideLoading()
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    (activity as StoryCoverActivity).hideLoading()
                    if (e.cause is APIActionException) {
                        if ((e.cause as APIActionException).code == 801) {
                            Toast.makeText(
                                activity,
                                (e.cause as APIActionException).message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            logOut()
                        } else {
                            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun logOut() {
        val fm: FragmentManager = childFragmentManager
        val dialog: ExpiredSessionDialog = ExpiredSessionDialog.newInstance()
        dialog.setOnEventDialogListener(object : ExpiredSessionDialog.OnEventDialogListener {
            override fun onClickApply() {
                dialog.dismiss()
                DedeSharedPref.saveUserInfo(null)
                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        })
        dialog.show(fm, dialog.tag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onEventDialogListener?.onRefreshData(hasUpdate)
    }

    fun setOnEventDialogListener(onEventDialogListener: OnEventDialogListener) {
        this.onEventDialogListener = onEventDialogListener
    }

    private var onEventDialogListener: OnEventDialogListener? = null

    interface OnEventDialogListener {
        fun onRefreshData(update: Boolean)
    }

}