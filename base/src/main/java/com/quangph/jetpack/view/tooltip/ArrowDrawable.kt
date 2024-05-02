package com.quangph.jetpack.view.tooltip

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.PixelFormat

import android.graphics.ColorFilter

import androidx.annotation.ColorInt

class ArrowDrawable(private val direction: Int, private val foreColor: Int) : ColorDrawable() {

    companion object {
        const val LEFT = 0
        const val TOP = 1
        const val RIGHT = 2
        const val BOTTOM = 3
    }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundColor = Color.TRANSPARENT
    private var path: Path? = null
    private var rectF = RectF()

    init {
        paint.color = foreColor
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        bounds.let {
            rectF.set(it)
            updatePath(rectF)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(backgroundColor)
        if (path == null) {
            rectF.set(bounds)
            updatePath(rectF)
        }
        canvas.drawPath(path!!, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColor(@ColorInt color: Int) {
        paint.color = color
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        if (paint.colorFilter != null) {
            return PixelFormat.TRANSLUCENT
        }
        when (paint.color ushr 24) {
            255 -> return PixelFormat.OPAQUE
            0 -> return PixelFormat.TRANSPARENT
        }
        return PixelFormat.TRANSLUCENT
    }

    @Synchronized
    private fun updatePath(bounds: RectF) {
        path = Path()
        when (direction) {
            LEFT -> {
                path?.moveTo(bounds.width(), bounds.height())
                path?.lineTo(0f, bounds.height() / 2)
                path?.lineTo(bounds.width(), 0f)
                path?.lineTo(bounds.width(), bounds.height())
            }
            TOP -> {
                path?.moveTo(0f, bounds.height())
                path?.lineTo(bounds.width() / 2, 0f)
                path?.lineTo(bounds.width(), bounds.height())
                path?.lineTo(0f, bounds.height())
            }
            RIGHT -> {
                path?.moveTo(0f, 0f)
                path?.lineTo(bounds.width(), bounds.height() / 2)
                path?.lineTo(0f, bounds.height())
                path?.lineTo(0f, 0f)
            }
            BOTTOM -> {
                path?.moveTo(0f, 0f)
                path?.lineTo(bounds.width() / 2, bounds.height())
                path?.lineTo(bounds.width(), 0f)
                path?.lineTo(0f, 0f)
            }
        }
        path?.close()
    }
}