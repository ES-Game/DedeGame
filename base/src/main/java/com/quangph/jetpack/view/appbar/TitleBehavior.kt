package com.quangph.jetpack.view.appbar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.quangph.base.R

/**
 * This view is a child of CoordinatorLayout. It is above the view with id: TitleBehavior_title_behavior_above_baseline.
 * When AppBarLayout collapsing/expanding this view scale up/down and move to replace the view with id: TitleBehavior_title_behavior_toolbar_anchor
 * Config view in xml: app:layout_behavior="com.quangph.jetpack.view.appbar.TitleBehavior"
 * Created by QuangPH on 2020-02-28.
 */
class TitleBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private var anchorId: Int = -1
    private var aboveBaselineId = -1
    private var isInit = false
    private var collapsedX = 0f
    private var totalXRange = 0f
    private var collapsedPortion = 1f
    private var expandedPortion = 1f
    private var viewAboveBaseline: View? = null

    init {
        if (attrs != null) {
            val a = context!!.obtainStyledAttributes(attrs, R.styleable.TitleBehavior)
            anchorId = a.getResourceId(R.styleable.TitleBehavior_title_behavior_toolbar_anchor, -1)
            aboveBaselineId = a.getResourceId(R.styleable.TitleBehavior_title_behavior_above_baseline, -1)
            val ratio = a.getString(R.styleable.TitleBehavior_title_behavior_collapsed_scale_ratio)
            ratio?.let {
                val splits = ratio.split(":")
                collapsedPortion = splits[0].toFloat()
                expandedPortion = splits[1].toFloat()
            }
            a.recycle()
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    var lastScaledRatio = 1f

    @SuppressLint("ObsoleteSdkInt")
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        initIfNeed(child, dependency as AppBarLayout)
        val maxScroll = totalScrollRange(dependency, child)

        var percentage = if (maxScroll != 0f) {
            Math.abs(dependency.getY()) / maxScroll.toFloat()
        } else {
            0f
        }
        if (percentage > 1f) {
            percentage = 1f
        }

        val scale = 1 - (expandedPortion - collapsedPortion) / expandedPortion * percentage
        child.scaleX = scale
        child.scaleY = scale

        val params = child.layoutParams as CoordinatorLayout.LayoutParams
        val y = (dependency.getHeight() + dependency.getY() - child.height - params.bottomMargin) - calculAboveBaseLine(dependency)
        child.y = y

        val scaledOffset = (1 - scale) * child.width / 2 //child.width - (expandedPortion - collapsedPortion) / expandedPortion * percentage * child.width
        child.x = totalXRange * (1 - percentage) + collapsedX - scaledOffset
        lastScaledRatio = scale

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (percentage < 1) {
                child.visibility = View.VISIBLE
            } else if (percentage == 1f) {
                child.visibility = View.GONE
            }
        }
        return true
    }

    private fun initIfNeed(child: View, appBarLayout: AppBarLayout) {
        if (!isInit) {
            isInit = true
            val anchorView = appBarLayout.findViewById<View>(anchorId)
            collapsedX = anchorView.x
            totalXRange = child.x - collapsedX
            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val maxScroll = totalScrollRange(appBarLayout, child)
                var percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()
                if (percentage > 1) {
                    percentage = 1f
                }

                if (percentage == 1f) {
                    anchorView.visibility = View.VISIBLE
                    child.visibility = View.INVISIBLE
                } else if (percentage < 1f) {
                    anchorView.visibility = View.GONE
                    child.visibility = View.VISIBLE
                }
            })

            viewAboveBaseline = appBarLayout.findViewById(aboveBaselineId)
        }
    }

    private fun totalScrollRange(appBarLayout: AppBarLayout, child: View): Float {
        val params = child.layoutParams as CoordinatorLayout.LayoutParams
        val maxScroll = appBarLayout.totalScrollRange - params.bottomMargin
        return if (viewAboveBaseline != null) {
            val range = appBarLayout.height - calculAboveBaseLine(appBarLayout) - child.height
            Math.min(maxScroll.toFloat(), range)
        } else {
            maxScroll.toFloat()
        }
    }

    private fun calculAboveBaseLine(appBarLayout: AppBarLayout): Float {
        return if (viewAboveBaseline == null) {
            0f
        } else {
            appBarLayout.height - viewAboveBaseline!!.y
        }
    }

}