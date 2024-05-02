package com.quangph.jetpack.imageloader.glide;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

/**
 * Created by QuangPH on 2020-12-04.
 */
public abstract class BitmapTransformation implements Transformation<Bitmap> {

    @NonNull
    public final Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
        if (!Util.isValidDimensions(outWidth, outHeight)) {
            throw new IllegalArgumentException("Cannot apply transformation on width: " + outWidth + " or height: " + outHeight + " less than or equal to zero and not Target.SIZE_ORIGINAL");
        } else {
            BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
            Bitmap toTransform = (Bitmap)resource.get();
            int targetWidth = outWidth == -2147483648 ? toTransform.getWidth() : outWidth;
            int targetHeight = outHeight == -2147483648 ? toTransform.getHeight() : outHeight;
            Bitmap transformed = this.transform(context.getApplicationContext(), bitmapPool, toTransform, targetWidth, targetHeight);
            Object result;
            if (toTransform.equals(transformed)) {
                result = resource;
            } else {
                result = BitmapResource.obtain(transformed, bitmapPool);
            }

            return (Resource)result;
        }
    }

    void setCanvasBitmapDensity(@NonNull Bitmap toTransform, @NonNull Bitmap canvasBitmap) {
        canvasBitmap.setDensity(toTransform.getDensity());
    }

    protected abstract Bitmap transform(@NonNull Context var1, @NonNull BitmapPool var2, @NonNull Bitmap var3, int var4, int var5);

    public abstract void updateDiskCacheKey(@NonNull MessageDigest var1);

    public abstract boolean equals(Object var1);

    public abstract int hashCode();
}
