package com.dede.dedegame.presentation.common.glide;

import android.content.ComponentName;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class GlideAppIconLoader implements ModelLoader<ComponentName, ComponentName> {
    public boolean handles(@NotNull ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "model");
        return true;
    }

    @Nullable
    public LoadData<ComponentName> buildLoadData(@NotNull ComponentName componentName, int i, int i2, @NotNull Options options) {
        return new LoadData<>(new ObjectKey(componentName), new IconDataFetcher(componentName));
    }

    public static final class IconDataFetcher implements DataFetcher<ComponentName> {
        @NotNull
        private final ComponentName model;

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        @Override
        @NotNull
        public Class<ComponentName> getDataClass() {
            return ComponentName.class;
        }

        public IconDataFetcher(@NotNull ComponentName componentName) {
            Intrinsics.checkNotNullParameter(componentName, "model");
            this.model = componentName;
        }

        @Override
        @NotNull
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override
        public void loadData(@NotNull Priority priority, @NotNull DataCallback<? super ComponentName> dataCallback) {
            dataCallback.onDataReady(this.model);
        }
    }
}
