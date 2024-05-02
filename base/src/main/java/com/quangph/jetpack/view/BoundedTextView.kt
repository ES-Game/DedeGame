package com.quangph.jetpack.view

import android.content.Context
import android.text.StaticLayout
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.quangph.base.R

/**
 * Textview width is measured by wrap a sample
 */
class BoundedTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    private var sample: String = ""
    private var widthText: Int = -1

    init {
        if (attrs != null) {
            init(attrs)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (widthText == -1) {
            widthText = measureText()
        }
        val spec = MeasureSpec.makeMeasureSpec(widthText + paddingStart + paddingEnd, MeasureSpec.EXACTLY)
        super.onMeasure(spec, heightMeasureSpec)
    }

    private fun measureText(): Int {
//        val bounds = Rect()
//        val str = this.sample
//        val fontStyle = typeface.style
//        if (fontStyle == Typeface.BOLD || fontStyle == Typeface.BOLD_ITALIC || fontStyle == Typeface.ITALIC) {
//            this.paint.typeface = Typeface.create(typeface, fontStyle)
//        }
//
//        this.paint.getTextBounds(str, 0, str.length, bounds)
//        return bounds.width()

        return StaticLayout.getDesiredWidth(sample, paint).toInt()
    }

    private fun init(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BoundedTextView
        )
        sample =
            typedArray.getString(R.styleable.BoundedTextView_bounded_text_view_sample) ?: ""
        typedArray.recycle()
    }
}