package com.quangph.jetpack.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.EditText
import androidx.annotation.NonNull
import com.quangph.base.R


/**
 * Edittext with a suffix label
 * Created by QuangPH on 2020-01-17.
 */
class SuffixEditText : androidx.appcompat.widget.AppCompatEditText {
    var textPaint = TextPaint()
    private var suffix: String = ""

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttributes(context, attrs, 0)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        textPaint.color = currentTextColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.LEFT
        if (compoundDrawablesRelative.isNotEmpty()) {
            setCompoundDrawables(compoundDrawablesRelative[0], null, TextDrawable(), null)
        } else {
            setCompoundDrawables(null, null, TextDrawable(), null)
        }
    }

    private fun getAttributes(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SuffixEditText, defStyleAttr, 0)
        if (a != null) {
            suffix = a.getString(R.styleable.SuffixEditText_suffix) ?: ""
            a.recycle()
        }
    }


    private inner class TextDrawable internal constructor() : Drawable() {

        override fun draw(@NonNull canvas: Canvas) {
            val lineBottom: Int = getLineBounds(lineCount - 1, null)
            val paint: Paint = getPaint()
            paint.color = Color.BLACK
            canvas.drawText(suffix, 0f, (canvas.clipBounds.top + lineBottom).toFloat(), paint)
        }

        override fun setAlpha(alpha: Int) {
        }

        override fun getOpacity(): Int {
            return PixelFormat.OPAQUE
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
        }

        init {
            setPadding(0, 0, paint.measureText(suffix).toInt() - 2, paddingBottom)
        }
    }
}