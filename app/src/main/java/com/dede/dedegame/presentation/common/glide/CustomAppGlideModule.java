package com.dede.dedegame.presentation.common.glide;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVG;
import com.dede.dedegame.presentation.common.glide.svg.SvgDecoder;
import com.dede.dedegame.presentation.common.glide.svg.SvgDrawableTranscoder;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import okhttp3.OkHttpClient;


@GlideModule
public class CustomAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        if (isLowRamDevice(context)) {
            builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        } else {
            builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
        }
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(okHttpClient);



        registry.replace(GlideUrl.class, InputStream.class, factory);
        registry.append(ComponentName.class, ComponentName.class, new GlideAppIconFactory());
        registry.append(ComponentName.class, Drawable.class, new GlideAppIconDecoder(context));
        registry.register(SVG.class, PictureDrawable.class, new SvgDrawableTranscoder())
                .append(InputStream.class, SVG.class, new SvgDecoder());
    }


    public boolean isLowRamDevice(@NotNull Context context) {
        Object systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return ActivityManagerCompat.isLowRamDevice((ActivityManager) systemService);
    }
}
