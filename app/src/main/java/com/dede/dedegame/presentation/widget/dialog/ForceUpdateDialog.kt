package com.dede.dedegame.presentation.widget.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.VersionUpdate
import com.google.gson.Gson

class ForceUpdateDialog : DialogFragment() {
    lateinit var viewParent: View
    lateinit var ivClose: ImageView
    lateinit var tvTitle: TextView
    lateinit var tvContent: TextView
    lateinit var btnPositive: AppCompatButton
    private var versionUpdate: VersionUpdate? = null

    companion object {
        fun newInstance(data: String): ForceUpdateDialog {
            val frag =
                ForceUpdateDialog()
            val args = Bundle()
            args.putString("key_data", data)
            frag.setArguments(args)
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.CommonDialog)
        arguments?.let {
            versionUpdate = Gson().fromJson(it.getString("key_data"), VersionUpdate::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewParent = inflater.inflate(R.layout.dialog_inform_update, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.color.color_background_common_dialog)
        dialog?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_background_common_dialog)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        initView()
        return viewParent
    }

    private fun initView() {
        ivClose = viewParent.findViewById(R.id.iv_close)
        tvTitle = viewParent.findViewById(R.id.tvTitle)
        tvContent = viewParent.findViewById(R.id.dialog_update_inform_message)
        btnPositive = viewParent.findViewById(R.id.popup_btn_positive)

        versionUpdate?.let {
            if (it.canClose) {
                ivClose.visibility = View.VISIBLE
            } else {
                ivClose.visibility = View.INVISIBLE
            }
            ivClose.setOnClickListener {
                dismiss()
            }

            if (it.title != null) {
                tvTitle.text = it.title
            } else {
                tvTitle.text = ""
            }

            if (it.content != null) {
                tvContent.text = it.content
            } else {
                tvContent.text = ""
            }

            if (it.nameAction != null) {
                btnPositive.text = it.nameAction
            } else {
                tvContent.text = ""
            }

            btnPositive.setOnClickListener {
                dismiss()
                if (onEventDialogListener != null) {
                    onEventDialogListener!!.onClickUpdate(versionUpdate!!.linkAction!!)
                }
            }
        }
    }

    fun setOnEventDialogListener(onEventDialogListener: OnEventDialogListener) {
        this.onEventDialogListener = onEventDialogListener
    }

    private var onEventDialogListener: OnEventDialogListener? = null

    interface OnEventDialogListener {
        fun onClickUpdate(url: String)
    }
}