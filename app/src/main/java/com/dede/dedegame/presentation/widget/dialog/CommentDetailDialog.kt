package com.dede.dedegame.presentation.widget.dialog

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.usecase.GetCommentByStoryId
import com.dede.dedegame.presentation.common.CustomItemDecoration
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListAdapter
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.dede.dedegame.presentation.story_cover.comment.adapter.CommentListAdapter
import com.dede.dedegame.presentation.widget.EndlessRecyclerViewScrollListener
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

    private var commentListAdapter = CommentListAdapter(false)
    private val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    private var comments: List<Comment>? = null
    private var comment: Comment? = null
    private var mStoryId: Int? = null
    private var currentPage = 1

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
            comment = Gson().fromJson(it.getString(KEY_COMMENT_ITEM), Comment::class.java)
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
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            editText.post {
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(
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
                if (s?.length!! > 0) {
                    setStateSendButton(true)
                } else {
                    setStateSendButton(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        if (comment != null) {
            val userTag = "${comment?.user}" + " "
            editText.setText(userTag)
            editText.setSelection(userTag.length)
            setStateSendButton(true)
        } else {
            setStateSendButton(false)
        }

        rootView.setOnClickListener {
            hideKeyboard()
        }

        if (comments.isNullOrEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            rvComments.adapter = commentListAdapter
            rvComments.layoutManager = layoutManager
            rvComments.addItemDecoration(
                CustomItemDecoration(
                    requireContext(),
                    R.dimen.margin_top_bottom_decorate,
                    R.dimen.margin_left_right_decorate
                )
            )
            rvComments.setItemAnimator(null)
            commentListAdapter.setListStory(comments!!)
        }
        rvComments.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (activity is StoryCoverActivity) {
                    currentPage += 1
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
                                responseValue?.dataList?.let {
                                    loadMore(flattenComments(it), responseValue.hasNextPage)
                                }
                            }

                            override fun onError(e: ActionException) {
                                super.onError(e)
                                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                }
            }
        })
    }

    private fun flattenComments(comments: List<Comment>, level: Int = 0): List<Comment> {
        val flatList = mutableListOf<Comment>()
        for (comment in comments) {
            comment.tab = level
            flatList.add(comment)
            comment.children?.let {
                flatList.addAll(flattenComments(it, level + 1))
            }
        }
        return flatList
    }

    private fun loadMore(data: List<Comment>, hasLoadMore: Boolean) {
        commentListAdapter.setLoadMore(hasLoadMore)
        if (hasLoadMore) {
            commentListAdapter.addItemsAndNotify(data)
        } else {
            val viewHolder =
                rvComments.findViewHolderForAdapterPosition(commentListAdapter.getEndPosListStory())
            if (viewHolder != null && viewHolder is StoryListAdapter.LoadingVH) {
                viewHolder.itemView.visibility = View.GONE
            }
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

    fun setOnEventDialogListener(onEventDialogListener: OnEventDialogListener) {
        this.onEventDialogListener = onEventDialogListener
    }

    private var onEventDialogListener: OnEventDialogListener? = null

    interface OnEventDialogListener
}