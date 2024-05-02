package com.quangph.base.view.recyclerview.adapter.group;

import android.view.View;

import androidx.annotation.NonNull;


import com.quangph.base.view.recyclerview.adapter.BaseRclvAdapter;
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder;

import java.util.List;

/**
 * Created by Pham Hai Quang on 7/27/2019.
 */
public class GroupRclvAdapter extends BaseRclvAdapter {
    protected GroupManager mGroupManager;

    public GroupRclvAdapter() {
        mGroupManager = new GroupManager(this);
    }

    @Override
    public int getItemCount() {
        return mGroupManager.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mGroupManager.getItemViewType(position);
    }

    @Override
    public int getLayoutResource(int viewType) {
        return mGroupManager.getLayoutResource(viewType);
    }

    @Override
    public BaseRclvHolder onCreateVH(View itemView, int viewType) {
        return mGroupManager.onCreateVH(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRclvHolder baseRclvHolder, int position) {
        if (baseRclvHolder instanceof GroupRclvVH) {
            mGroupManager.onBindViewHolder((GroupRclvVH) baseRclvHolder, position);
        } else {
            super.onBindViewHolder(baseRclvHolder, position);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BaseRclvHolder baseRclvHolder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty() && baseRclvHolder instanceof GroupRclvVH) {
            mGroupManager.onBindViewHolder((GroupRclvVH) baseRclvHolder, position, payloads);
        } else {
            super.onBindViewHolder(baseRclvHolder, position, payloads);
        }
    }

    @Override
    public Object getItemDataAtPosition(int position) {
        return mGroupManager.getItemDataAtAdapterPosition(position);
    }

    public void notifyAllGroupChanged() {
        mGroupManager.notifyDataSetChanged();
    }

    public void addGroup(GroupData data) {
        mGroupManager.addGroupData(data);
    }

    public void addGroupDataAtIndex(int index, GroupData data) {
        mGroupManager.addGroupDataAtIndex(index, data);
    }

    public void removeGroup(GroupData groupData) {
        if (groupData != null) {
            mGroupManager.removeGroup(groupData);
        }
    }

    public void removeGroupWithoutNotify(GroupData groupData) {
        if (groupData != null) {
            mGroupManager.removeGroupWithoutNotify(groupData);
        }
    }

    public void clear() {
        mGroupManager.clear();
    }

    public GroupManager getGroupManager() {
        return mGroupManager;
    }

    public int getPositionInGroup(int adapterPosition) {
        GroupData group = mGroupManager.findGroupDataByAdapterPosition(adapterPosition);
        if (group != null) {
            return adapterPosition - group.adapterPosition;
        }

        return -1;
    }

    public int getIndexOfGroup(GroupData groupData) {
        return mGroupManager.getIndexOfGroupData(groupData);
    }

    public void addRawGroupAtIndex(int index, GroupData groupData) {
        mGroupManager.addRawGroupDataAtIndex(index, groupData);
    }
}
