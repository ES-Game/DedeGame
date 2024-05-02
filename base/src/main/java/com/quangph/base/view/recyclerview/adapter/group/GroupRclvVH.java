package com.quangph.base.view.recyclerview.adapter.group;

import android.view.View;

import androidx.annotation.NonNull;

import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder;


public class GroupRclvVH<T, GD extends GroupData> extends BaseRclvHolder<T> {

    public GD groupData;

    public GroupRclvVH(@NonNull View itemView) {
        super(itemView);
    }
}
