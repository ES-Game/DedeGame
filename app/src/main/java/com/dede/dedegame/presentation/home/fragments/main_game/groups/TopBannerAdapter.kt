package com.dede.dedegame.presentation.home.fragments.main_game.groups

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Slider
import com.quangph.base.view.recyclerview.adapter.BaseRclvAdapter


class TopBannerAdapter : BaseRclvAdapter(){
    var presenter: IPresenter? = null

    override fun getLayoutResource(p0: Int): Int = R.layout.item_home_top_slider

    override fun onCreateVH(p0: View?, p1: Int): BaseRclvHolder<*> = TopBannerVH(p0!!, this)

    override fun getItemCount(): Int = mDataSet?.size ?: 0

    override fun getItemDataAtPosition(position: Int): Any? = mDataSet?.get(position)

    private class TopBannerVH(itemView: View, val adapter: TopBannerAdapter) :
        BaseRclvHolder<Slider>(itemView) {

        private var ivBanner: ImageView

        init {
            ivBanner = itemView.findViewById(R.id.ivHomeTopSliderItmBanner)

            ivBanner.setOnClickListener {
//                adapter.presenter?.executeCommand(
//
//                )
            }
        }

        @SuppressLint("ResourceAsColor")
        override fun onBind(vhData: Slider?) {
            super.onBind(vhData)
            vhData?.let {
                Glide
                    .with(ivBanner.context)
                    .load(it.image)
                    .centerCrop()
                    .into(ivBanner);
                try {
                    ivBanner.setBackgroundColor(R.color.gray_300)
                } catch (e: Exception) {
                    e.printStackTrace()
                    ivBanner.setBackgroundColor(ContextCompat.getColor(ivBanner.context, R.color.gray_300))
                }
            }
        }
    }
}