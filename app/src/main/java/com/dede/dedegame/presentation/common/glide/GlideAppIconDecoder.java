package com.dede.dedegame.presentation.common.glide;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.util.Util;
import com.dede.dedegame.presentation.common.LogUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;


public class GlideAppIconDecoder implements ResourceDecoder<ComponentName, Drawable> {
    @NotNull
    private final Context context;

    public boolean handles(@NotNull ComponentName componentName, @NotNull Options options) {
        return true;
    }

    public GlideAppIconDecoder(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    @Nullable
    public Resource<Drawable> decode(@NotNull ComponentName componentName, int i, int i2, @NotNull Options options) {
        Intrinsics.checkNotNullParameter(componentName, "source");
        Intrinsics.checkNotNullParameter(options, "options");
        PackageManager packageManager = this.context.getPackageManager();
        try {
            return new IconResource(packageManager.getActivityInfo(componentName, 0).applicationInfo.loadIcon(packageManager));
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.getInstance().e(e);
            return null;
        }
    }

    public static final class IconResource extends DrawableResource<Drawable> {

        public IconResource(Drawable drawable) {
            super(drawable);
            Intrinsics.checkNotNullParameter(drawable, "icon");
        }

        @NonNull
        @Override
        public Class<Drawable> getResourceClass() {
            return Drawable.class;
        }

        @Override
        public int getSize() {
            Drawable t = this.drawable;
            if (t instanceof BitmapDrawable) {
                return Util.getBitmapByteSize(((BitmapDrawable) t).getBitmap());
            }
            return 1;
        }

        @Override
        public void recycle() {

        }
    }
}