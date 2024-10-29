package com.dede.dedegame.presentation.common.glide;

import android.content.ComponentName;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

public class GlideAppIconFactory implements ModelLoaderFactory<ComponentName, ComponentName> {

    @NonNull
    @Override
    public ModelLoader<ComponentName, ComponentName> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new GlideAppIconLoader();
    }

    @Override
    public void teardown() {

    }
}
