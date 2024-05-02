package com.quangph.jetpack.imageloader.glide

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.quangph.jetpack.imageloader.IImageLoader
import com.quangph.jetpack.imageloader.IImageLoader.CornerType
import java.io.File

/**
 * Created by Pham Hai Quang on 11/10/2019.
 */

object GlideImageLoader : IImageLoader {

    override fun loadImage(activity: Activity?, url: String?, view: ImageView?, ignoreCache: Boolean) {
        Glide.with(activity!!).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.into(view!!)
    }

    override fun loadImage(fragment: Fragment?, url: String?, view: ImageView?, ignoreCache: Boolean) {
        Glide.with(fragment!!).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.into(view!!)
    }

    override fun loadImage(withView: View?,
                           url: String?,
                           view: ImageView?,
                           errorDrawable: Drawable?,
                           ignoreCache: Boolean) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).setDefaultRequestOptions(requestOptions).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.into(view!!)
    }

    override fun loadImage(withView: View?,
                           url: String?,
                           view: ImageView?,
                           errorDrawable: Drawable?,
                           ignoreCache: Boolean,
                           onError: () -> Unit, onComplete: () -> Unit
    ) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).setDefaultRequestOptions(requestOptions).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                onError.invoke()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onComplete.invoke()
                return false
            }

        })
                .into(view!!)
    }

    override fun loadRoundCornerImage(withView: View?, url: String?, view: ImageView?, corner: Int) {
        Glide.with(withView!!).load(url).apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(corner))).into(view!!)
    }

    override fun loadRoundCornerImage(withView: View?, url: String?, view: ImageView?, corner: Int, cornerType: CornerType?) {
        Glide.with(withView!!).load(url)
                .apply(RequestOptions().transforms(CenterCrop(),
                    RoundedCornersTransformation(
                        corner, 0,
                        RoundedCornersTransformation.CornerType.valueOf(
                            cornerType.toString()
                        )
                    )
                ))
                .into(view!!)
    }

    override fun loadRoundCornerImage(withView: View?, url: String?, view: ImageView?, corner: Int, cornerType: CornerType?, errorDrawable: Drawable?) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).load(url)
                .apply(requestOptions.transforms(CenterCrop(),
                    RoundedCornersTransformation(
                        corner, 0,
                        RoundedCornersTransformation.CornerType.valueOf(
                            cornerType.toString()
                        )
                    )
                ))
                .into(view!!)
    }

    override fun loadRoundCornerImage(withView: View?, url: String?, view: ImageView?, corner: Int, cornerType: CornerType?, errorDrawable: Drawable?, onError: () -> Unit, onComplete: () -> Unit) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).load(url)
                .apply(
                        requestOptions.transforms(CenterCrop(),
                            RoundedCornersTransformation(
                                corner,
                                0,
                                RoundedCornersTransformation.CornerType.valueOf(
                                    cornerType.toString()
                                )
                            )
                        )
                )
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                        onError.invoke()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        onComplete.invoke()
                        return false
                    }

                })
                .into(view!!)
    }

    override fun loadCircleImage(withView: View?, url: String?, view: ImageView?) {
        Glide.with(withView!!).load(url).apply(RequestOptions.circleCropTransform()).into(view!!)
    }

    override fun loadCircleImage(withView: View?, file: File?, view: ImageView?) {
        Glide.with(withView!!).load(file).apply(RequestOptions.circleCropTransform()).into(view!!)
    }

    override fun loadCircleImage(withView: View?, bitmap: Bitmap?, view: ImageView?) {
        Glide.with(withView!!).load(bitmap).apply(RequestOptions.circleCropTransform()).into(view!!)
    }

    override fun loadCircleImage(withView: View?, url: String?, view: ImageView?, errorDrawable: Drawable?) {
        Glide.with(withView!!).load(url).apply(RequestOptions.circleCropTransform().error(errorDrawable)).into(view!!)
    }

    override fun loadImageNoCache(withView: View?, url: String?, view: ImageView?, errorDrawable: Drawable?) {
        Glide.with(withView!!).load(url)
                .error(errorDrawable)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view!!)
    }

    override fun loadBitmap(activity: Activity?, url: String?, onResult: (Bitmap) -> Unit) {
        Glide.with(activity!!).asBitmap().load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        onResult.invoke(resource)
                    }
                })
    }

}