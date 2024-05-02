package com.quangph.jetpack.imageloader

import com.quangph.jetpack.imageloader.glide.GlideImageLoader

/**
 * Created by QuangPH on 2020-03-18.
 */
object ImageLoaderFactory {

    fun glide(): IImageLoader {
        return GlideImageLoader
    }
}