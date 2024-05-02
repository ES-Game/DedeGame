package com.quangph.jetpack.view

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class ZoomImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private val MIN_SCALE = 1f
    private val NONE = 0
    private val ZOOM = 1
    private val DISTANCE_ZOOM = 5f

    var onStartTouchListener: (() -> Unit)? = null

    var onStopTouchListener: (() -> Unit)? = null

    private var matrixSet: Matrix = Matrix()

    private var matrixSaved: Matrix = Matrix()

    private var pointDown = PointF(0f, 0f)

    private var pointPre = PointF(0f, 0f)

    private val matrixValues = FloatArray(9)

    private var mode = NONE

    private var midPoint: PointF = PointF(0f, 0f)

    private var distanceOld = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action?.and(MotionEvent.ACTION_MASK)) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                this.scaleType = ScaleType.MATRIX
                onStartTouchListener?.invoke()
                distanceOld = distance(event)
                if (distanceOld > DISTANCE_ZOOM) {
                    mode = ZOOM
                    imageMatrix.getValues(matrixValues)
                    if (matrixValues[Matrix.MSCALE_X] == MIN_SCALE) {
                        setDefaultScaleTypeMatrix()
                    }
                    matrixSaved.set(matrixSet)
                    midPoint.mid(event)
                }

            }
            MotionEvent.ACTION_DOWN -> {
                pointDown.set(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                if (mode == ZOOM && event.pointerCount > 1) {
                    val newDis = distance(event)
                    if (newDis > DISTANCE_ZOOM) {
                        matrixSet.set(matrixSaved)
                        val scale = newDis / distanceOld
                        matrixSet.postScale(scale, scale, pointDown.x, pointDown.y)
                    }
                }
                matrixSet.postTranslate(event.x - pointPre.x, event.y - pointPre.y)
                fixTransition()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                val matrixValues = FloatArray(9)
                matrixSet.getValues(matrixValues)
                val scale = max(matrixValues[Matrix.MSCALE_X], matrixValues[Matrix.MSCALE_Y])
                if (scale <= MIN_SCALE) {
                    matrixSet.reset()
                    scaleType = ScaleType.CENTER_INSIDE
                    onStopTouchListener?.invoke()
                    return true
                }
            }
        }
        pointPre.set(event?.x ?: 0f, event?.y ?: 0f)
        imageMatrix = matrixSet
        return true
    }

    private fun fixTransition() {
        if (mode != NONE) return
        matrixSet.getValues(matrixValues)
        val corX = matrixValues[Matrix.MTRANS_X]
        val corY = matrixValues[Matrix.MTRANS_Y]

        val scaleX = matrixValues[Matrix.MSCALE_X]
        val scaleY = matrixValues[Matrix.MSCALE_Y]

        val dWidth = (drawable?.intrinsicWidth ?: 0).toFloat()
        val dHeight = (drawable?.intrinsicHeight ?: 0).toFloat()

        val vWidth = measuredWidth.toFloat()
        val vHeight = measuredHeight.toFloat()

        val widthScale = dWidth * scaleX
        val heightScale = dHeight * scaleY

        if (corX > 0f) {
            if (widthScale < vWidth) {
                matrixValues[Matrix.MTRANS_X] = abs(widthScale - vWidth) / 2
            } else {
                matrixValues[Matrix.MTRANS_X] = 0f
            }
        }

        if ( corX < 0 && corX < vWidth - widthScale) {
            matrixValues[Matrix.MTRANS_X] = vWidth - widthScale
        }

        if (corY > 0f) {
            if (heightScale < vHeight) {
                matrixValues[Matrix.MTRANS_Y] = abs(heightScale - vHeight) / 2
            } else {
                matrixValues[Matrix.MTRANS_Y] = 0f
            }
        }

        if ( corY < 0 && corY < vHeight - heightScale) {
            matrixValues[Matrix.MTRANS_Y] = vHeight - heightScale
        }

        matrixSet.setValues(matrixValues)
    }

    private fun setDefaultScaleTypeMatrix() {
        val dWidth = (drawable?.intrinsicWidth ?: 0).toFloat()
        val dHeight = (drawable?.intrinsicHeight ?: 0).toFloat()

        val vWidth = measuredWidth.toFloat()
        val vHeight = measuredHeight.toFloat()

        imageMatrix.getValues(matrixValues)

        matrixValues[Matrix.MTRANS_X] = (vWidth - dWidth) / 2f

        matrixValues[Matrix.MTRANS_Y] = (vHeight - dHeight) / 2f

        matrixSet.setValues(matrixValues)
    }

    private fun distance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    private fun PointF.mid(event: MotionEvent) {
        this.x = (event.getX(0) + event.getX(1)) / 2f
        this.y = (event.getY(0) + event.getY(1)) / 2f
    }

}