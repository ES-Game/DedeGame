package com.dede.dedegame.presentation.widget.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.dede.dedegame.R

class ExpiredSessionDialog : DialogFragment() {
    lateinit var viewParent: View
    lateinit var btnCancel : AppCompatButton
    lateinit var btnApply : AppCompatButton
    companion object {
        fun newInstance(): ExpiredSessionDialog {
            val frag =
                ExpiredSessionDialog()
            val args = Bundle()
            frag.setArguments(args)
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.CommonDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewParent = inflater.inflate(R.layout.dialog_expired_session, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.color.color_background_common_dialog)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        initView()
        return viewParent
    }

    private fun initView() {
        btnCancel = viewParent.findViewById(R.id.btnCancel)
        btnApply = viewParent.findViewById(R.id.btnApply)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnApply.setOnClickListener {
            if (onEventDialogListener != null){
                onEventDialogListener!!.onClickApply()
            }
        }
    }

    fun setOnEventDialogListener(onEventDialogListener : OnEventDialogListener){
        this.onEventDialogListener = onEventDialogListener
    }

    private var onEventDialogListener : OnEventDialogListener ?= null

    interface OnEventDialogListener{
        fun onClickApply()
    }

}