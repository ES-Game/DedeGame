package com.dede.dedegame.presentation.home.fragments.home.groups

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.presentation.common.IndicatorView
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class TopBannerGroupData(listStory: List<Slider>?) :
    GroupData<List<Slider>>(listStory) {
    var mPresenter: IPresenter? = null
    var sliderHandler: Handler = Handler(Looper.getMainLooper())
    var onEvenSliderListener: OnEvenSliderListener? = null
    private val delayMillis: Long = 3000
    private var runnable: Runnable? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return if (data != null) 1 else 0
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.TOP_BANNER
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH", "")
        if (viewType == ItemViewType.TOP_BANNER) {
            Log.i("onCreateVH", "TOP_BANNER")
            return TopBannerVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.TOP_BANNER) {
            R.layout.item_home_top_banner
        } else {
            INVALID_RESOURCE
        }
    }

    fun cancelSlider() {
        sliderHandler.removeCallbacksAndMessages(null)
    }

    private class TopBannerVH(itemView: View, val topBannerGroupData: TopBannerGroupData) :
        GroupRclvVH<List<Slider>, TopBannerGroupData>(itemView) {

        private var vpHomeTopBannerItm: ViewPager2
        private var idvHomeTopBannerItm: IndicatorView
        private var adapter: TopBannerAdapter

        init {
            vpHomeTopBannerItm = itemView.findViewById(R.id.vpHomeTopBannerItm)
            idvHomeTopBannerItm = itemView.findViewById(R.id.idvHomeTopBannerItm)

            adapter = TopBannerAdapter()
            adapter.onEvenSliderListener = object : TopBannerAdapter.OnEvenSliderListener {
                override fun onClickSliderItem(item: Slider) {
                    topBannerGroupData.onEvenSliderListener?.onClickSliderItem(item)
                }
            }
            adapter.presenter = topBannerGroupData.mPresenter
            vpHomeTopBannerItm.adapter = adapter

            idvHomeTopBannerItm.setUpWithViewPager2(vpHomeTopBannerItm, false)
            slideBanner()
        }

        override fun onBind(vhData: List<Slider>?) {
            super.onBind(vhData)
            vhData?.let { sliders ->
                if (adapter.mDataSet.isNullOrEmpty()) {
                    adapter.reset(sliders)
                }
            }
        }

        private fun slideBanner() {
            topBannerGroupData.runnable = object : Runnable {
                override fun run() {
                    val currentItem = vpHomeTopBannerItm.currentItem
                    val nextItem = if (currentItem == adapter.itemCount - 1) 0 else currentItem + 1
                    vpHomeTopBannerItm.setCurrentItem(nextItem, true)
                    topBannerGroupData.sliderHandler.postDelayed(this, topBannerGroupData.delayMillis)
                }
            }
            topBannerGroupData.sliderHandler.postDelayed(topBannerGroupData.runnable!!, topBannerGroupData.delayMillis)

            vpHomeTopBannerItm.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        topBannerGroupData.sliderHandler.removeCallbacks(topBannerGroupData.runnable!!)
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        topBannerGroupData.sliderHandler.postDelayed(topBannerGroupData.runnable!!, topBannerGroupData.delayMillis)
                    }
                }
            })
        }
    }

    interface OnEvenSliderListener {
        fun onClickSliderItem(item: Slider)
    }
}