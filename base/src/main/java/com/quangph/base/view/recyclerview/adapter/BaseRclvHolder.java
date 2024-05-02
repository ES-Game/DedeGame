package com.quangph.base.view.recyclerview.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quangph.base.view.SafeClicked;

import java.util.List;

/**
 * Created by Pham Hai Quang on 1/11/2019.
 */
public class BaseRclvHolder<T> extends RecyclerView.ViewHolder {
    public BaseRclvHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void clickOn(View view, final View.OnClickListener listener) {
        if (listener != null) {
            view.setOnClickListener(new SafeClicked() {
                @Override
                public void onSafeClicked(View view) {
                    if (getAdapterPosition() > -1) {
                        listener.onClick(view);
                    }
                }
            });
        }
    }

    public void onBind(T vhData) {}
    public void onBind(T vhData, @NonNull List<Object> payloads) {}
}
