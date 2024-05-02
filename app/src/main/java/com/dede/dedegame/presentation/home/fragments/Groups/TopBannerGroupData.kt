package com.dede.dedegame.presentation.home.fragments.Groups

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.R
import com.quangph.dedegame.domain.model.Story
import com.dede.dedegame.presentation.home.fragments.Groups.TopBannerAdapter
import com.dede.dedegame.presentation.commom.IndicatorView

class TopBannerGroupData(listStory: List<Story>?) :
    GroupData<List<Story>>(listStory) {
    var mPresenter: IPresenter? = null
    var sliderHandler: Handler = Handler(Looper.getMainLooper())
    var currentBannerPosition = 1

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
        GroupRclvVH<List<Story>, TopBannerGroupData>(itemView) {

        private var vpHomeTopBannerItm: ViewPager2
        private var idvHomeTopBannerItm: IndicatorView
        private var adapter: TopBannerAdapter
        private var sliderRun: Runnable
        private val delaySlideTime = 5000L

        init {
            vpHomeTopBannerItm = itemView.findViewById(R.id.vpHomeTopBannerItm)
            idvHomeTopBannerItm = itemView.findViewById(R.id.idvHomeTopBannerItm)

            adapter = TopBannerAdapter()
            adapter.presenter = topBannerGroupData.mPresenter
            vpHomeTopBannerItm.adapter = adapter

            sliderRun = Runnable {
                if (adapter.itemCount > 1) {
                    topBannerGroupData.currentBannerPosition = vpHomeTopBannerItm.currentItem + 1
                    if (topBannerGroupData.currentBannerPosition >= adapter.itemCount - 1) {
                        topBannerGroupData.currentBannerPosition = 1
                        vpHomeTopBannerItm.setCurrentItem(
                            topBannerGroupData.currentBannerPosition,
                            false
                        )
                    } else {
                        vpHomeTopBannerItm.currentItem = topBannerGroupData.currentBannerPosition
                    }
                }
            }

            idvHomeTopBannerItm.setUpWithViewPager2(vpHomeTopBannerItm, false)

            vpHomeTopBannerItm.registerOnPageChangeCallback(object : OnPageChangeCallback() {

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE && adapter.itemCount > 1) {
                        when (vpHomeTopBannerItm.currentItem) {
                            adapter.itemCount - 1 -> {
                                topBannerGroupData.currentBannerPosition = 1
                                vpHomeTopBannerItm.setCurrentItem(
                                    topBannerGroupData.currentBannerPosition, false
                                )
                            }
                            0 -> {
                                topBannerGroupData.currentBannerPosition = adapter.itemCount - 2
                                vpHomeTopBannerItm.setCurrentItem(
                                    topBannerGroupData.currentBannerPosition, false
                                )
                            }

                            else -> topBannerGroupData.currentBannerPosition =
                                vpHomeTopBannerItm.currentItem
                        }
                    }
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    slideBanner()
                }
            })
        }

        override fun onBind(vhData: List<Story>?) {
            super.onBind(vhData)
            vhData?.let { userPromotionList ->
                if (adapter.mDataSet.isNullOrEmpty()) {
                    adapter.reset(userPromotionList)
                }
                if (adapter.itemCount > 1) {
                    when {
                        topBannerGroupData.currentBannerPosition >= adapter.itemCount - 1 -> {
                            topBannerGroupData.currentBannerPosition = 1
                        }
                        topBannerGroupData.currentBannerPosition == 0 -> {
                            topBannerGroupData.currentBannerPosition = adapter.itemCount - 2
                        }
                    }
                    vpHomeTopBannerItm.post {
                        vpHomeTopBannerItm.setCurrentItem(
                            topBannerGroupData.currentBannerPosition,
                            false
                        )
                    }
                    slideBanner()
                }
            }
        }

        private fun slideBanner() {
            topBannerGroupData.sliderHandler.removeCallbacks(sliderRun)
            topBannerGroupData.sliderHandler.postDelayed(sliderRun, delaySlideTime)
        }
    }
}