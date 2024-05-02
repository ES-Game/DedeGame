package com.quangph.jetpack.view.tooltip

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.quangph.base.R

abstract class Tooltip(context: Context) : Dialog(context, R.style.ToolTip) {

    private var tcvContainer: TooltipContainerView2? = null
    private var anchorView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        tcvContainer = TooltipContainerView2(context)
        tcvContainer?.cornerRadius = getTooltipCornerRadius()
        tcvContainer?.bgColor = getTooltipBackgroundColor()
        tcvContainer?.setContentView(getContentLayoutID())
        tcvContainer?.setBackgroundColor(Color.TRANSPARENT)

        setContentView(tcvContainer!!)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        tcvContainer?.setOnClickListener {
            dismiss()
        }

        tcvContainer?.anchorView = this.anchorView
    }

    abstract fun getContentLayoutID(): Int

    open fun getTooltipBackgroundColor(): Int {
        return Color.BLACK
    }

    open fun getTooltipCornerRadius(): Float {
        return 0f
    }

    fun show(anchorView: View) {
        this.anchorView = anchorView
        show()
    }
}