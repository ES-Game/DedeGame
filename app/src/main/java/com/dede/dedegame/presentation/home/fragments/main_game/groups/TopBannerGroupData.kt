package com.dede.dedegame.presentation.home.fragments.main_game.groups

import android.util.Log
import android.view.View
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.presentation.common.IndicatorView
import com.dede.dedegame.presentation.widget.carouselView.CarouselView
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class TopBannerGroupData(listStory: List<Slider>?) :
    GroupData<List<Slider>>(listStory) {
    var mPresenter: IPresenter? = null

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
            R.layout.item_main_game_top_banner
        } else {
            INVALID_RESOURCE
        }
    }

    private class TopBannerVH(itemView: View, val topBannerGroupData: TopBannerGroupData) :
        GroupRclvVH<List<Slider>, TopBannerGroupData>(itemView) {

        private var vpHomeTopBannerItm: CarouselView
        private var idvHomeTopBannerItm: IndicatorView

        init {
            vpHomeTopBannerItm = itemView.findViewById(R.id.vpHomeTopBannerItm)
            idvHomeTopBannerItm = itemView.findViewById(R.id.idvHomeTopBannerItm)
        }

        override fun onBind(vhData: List<Slider>?) {
            super.onBind(vhData)
            vhData?.let { sliders ->
                vpHomeTopBannerItm.setPageCount(sliders.size)
                vpHomeTopBannerItm.setImageListener { position, imageView ->
                    imageView?.loadImageFromUrl(
                        sliders[position].image
                    )
                }
                vpHomeTopBannerItm.setImageClickListener { position ->
                    topBannerGroupData.onEvenSliderListener?.onClickSliderItem(
                        sliders[position]
                    )
                }
                idvHomeTopBannerItm.setUpWithViewPager(vpHomeTopBannerItm)
            }
        }
    }

    var onEvenSliderListener: OnEvenSliderListener? = null

    interface OnEvenSliderListener {
        fun onClickSliderItem(item: Slider)
    }
}