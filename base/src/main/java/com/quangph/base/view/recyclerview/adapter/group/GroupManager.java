package com.quangph.base.view.recyclerview.adapter.group;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Pham Hai Quang on 7/27/2019.
 */
public class GroupManager {

    private RecyclerView.Adapter mAdapter;
    private List<GroupData> mDataSet = new ArrayList<>();

    public GroupManager(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Add a group to data set, but it is not attached mean its info is not be init.
     * After adding, adapter position is -1, so when you want to show this group, you need call GroupData.attach
     * @param data
     */
    public void addRawGroupDataAtIndex(int index, GroupData data) {
        data.setGroupManager(this);
        mDataSet.add(index, data);
    }

    public void addRawGroupData(GroupData data) {
        data.setGroupManager(this);
        mDataSet.add(data);
    }

    public void addGroupData(GroupData data) {
        data.setGroupManager(this);
        mDataSet.add(data);
        if (data.getCount() == 0) {
            data.detach();
        } else if (!data.isAttached()) {
            data.attach();
        }
    }

    public void addGroupDataAtIndex(int index, GroupData data) {
        data.setGroupManager(this);
        mDataSet.add(index, data);
        if (data.getCount() == 0) {
            data.detach();
        } else if (!data.isAttached()) {
            data.attach();
            shiftAdapterPosition(data, data.getCount());
        }
    }

    public void removeGroup(GroupData groupData) {
        if (mDataSet.contains(groupData)) {
            shiftAdapterPosition(groupData, -groupData.getCount());
            int currPosition = groupData.adapterPosition;
            groupData.detach();
            mDataSet.remove(groupData);
            mAdapter.notifyItemRangeRemoved(currPosition, groupData.getCount());
        }
    }

    public void removeGroupWithoutNotify(GroupData groupData) {
        if (mDataSet.contains(groupData)) {
            shiftAdapterPosition(groupData, -groupData.getCount());
            groupData.detach();
            mDataSet.remove(groupData);
        }
    }

    public List<GroupData> getDataSet() {
        return mDataSet;
    }

    public int getItemCount() {
        int total = 0;
        for (GroupData data : mDataSet) {
            if (data.isAttached()) {
                total += data.getCount();
            }
        }
        return total;
    }

    public int getLayoutResource(int viewType) {
        for (GroupData data : mDataSet) {
            int layoutResource = data.getLayoutResource(viewType);
            if (layoutResource != GroupData.INVALID_RESOURCE) {
                return layoutResource;
            }
        }
        throw new IllegalArgumentException("Can not find layout for type: " + viewType);
    }

    public int getItemViewType(int adapterPosition) {
        GroupData data = findGroupDataByAdapterPosition(adapterPosition);
        if (data != null) {
            return data.getItemViewType(adapterPosition - data.adapterPosition);
        } else {
            throw new IllegalArgumentException("Can not find data for position: " + adapterPosition);
        }
    }

    public BaseRclvHolder onCreateVH(View itemView, int viewType) {
        for (GroupData data : mDataSet) {
            BaseRclvHolder vh = data.onCreateVH(itemView, viewType);
            if (vh != null) {
                return vh;
            }
        }
        throw new IllegalArgumentException("Can not find ViewHolder for type: " + viewType);
    }

    public void onBindViewHolder(@NonNull GroupRclvVH vh, int position) {
        vh.groupData = findGroupDataByAdapterPosition(position);
        vh.onBind(getItemDataAtAdapterPosition(position));
    }

    public void onBindViewHolder(@NonNull GroupRclvVH vh, int position, @NonNull List<Object> payloads) {
        vh.groupData = findGroupDataByAdapterPosition(position);
        vh.onBind(getItemDataAtAdapterPosition(position), payloads);
    }

    public Object getItemDataAtAdapterPosition(int adapterPosition) {
        GroupData data = findGroupDataByAdapterPosition(adapterPosition);
        if (data != null) {
            return data.getDataInGroup(adapterPosition - data.adapterPosition);
        } else {
            throw new IllegalArgumentException("Can not find data for position: " + adapterPosition);
        }
    }

    public void notifyRemove(GroupData group, int adapterPosition) {
        shiftAdapterPosition(group, -1);
        mAdapter.notifyItemRemoved(adapterPosition);
        checkToDetach(group);
    }

    public void notifyRemove(GroupData group, int adapterPosition, int count) {
        shiftAdapterPosition(group, -count);
        mAdapter.notifyItemRangeRemoved(adapterPosition, count);
        checkToDetach(group);
    }

    public void notifyInserted(GroupData group, int adapterPosition, int count) {
        shiftAdapterPosition(group, count);
        mAdapter.notifyItemRangeInserted(adapterPosition, count);
    }

    public void notifyChanged(GroupData group, int adapterPosition) {
        mAdapter.notifyItemChanged(adapterPosition);
    }

    public void notifyChanged(GroupData group, int adapterPosition, Object payload) {
        mAdapter.notifyItemChanged(adapterPosition, payload);
    }

    public void notifyChanged(GroupData group) {
        mAdapter.notifyItemRangeChanged(group.adapterPosition, group.getCount());
    }

    public void notifyChanged(GroupData group, Object payload) {
        mAdapter.notifyItemRangeChanged(group.adapterPosition, group.getCount(), payload);
    }

    public void notifyChanged(int adapterPosition, int count, Object payload) {
        mAdapter.notifyItemRangeChanged(adapterPosition, count, payload);
    }

    public void notityNewGroupAdded(GroupData group) {
        if (!mDataSet.contains(group)) {
            return;
        }
        if (!group.isAttached()) {
            group.attach();
            shiftAdapterPosition(group, group.getCount());
        }
        mAdapter.notifyItemRangeInserted(group.adapterPosition, group.getCount());
    }

    public void notifyDataSetChanged() {
        for (GroupData group : mDataSet) {
            if (group.isAttached() && group.getCount() <= 0) {
                group.detach();
            } else {
//                if (group.getCount() > 0) {
//                    group.attach();
//                }

                if (group.isAttached()) {
                    group.adapterPosition = findAdapterPositionForGroup(group);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public GroupData findGroupDataByAdapterPosition(int adapterPosition) {
        for (GroupData data : mDataSet) {
            if (data.isAttached()) {
                if (data.adapterPosition <= adapterPosition && adapterPosition < data.adapterPosition + data.getCount()) {
                    return data;
                }
            }
        }
        return null;
    }

    public int findAdapterPositionForGroup(GroupData group) {
        int index = mDataSet.indexOf(group);
        if (index > 0) {
            for (int i = index - 1; i >= 0; i--) {
                GroupData prev = mDataSet.get(i);
                if (prev.isAttached()) {
                    return prev.adapterPosition + prev.getCount();
                }
            }
            return 0;
        } else if (index == 0) {
            return 0;
        } else {
            throw new IllegalArgumentException("The GroupData is not added");
        }
    }

    public int getIndexOfGroupData(GroupData groupData) {
        if (groupData == null) {
            return -1;
        }
        return mDataSet.indexOf(groupData);
    }

    public void clear() {
        Iterator<GroupData> itr = mDataSet.iterator();
        while (itr.hasNext()) {
            GroupData next = itr.next();
            shiftAdapterPosition(next, -next.getCount());
            next.detach();
            itr.remove();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void shiftAdapterPosition(GroupData startGroup, int count) {
        int startIndex = mDataSet.indexOf(startGroup);
        for (int i = startIndex + 1; i < mDataSet.size(); i++) {
            GroupData next = mDataSet.get(i);
            if (next.isAttached()) {
                next.adapterPosition += count;
            }
        }
    }

    private void checkToDetach(GroupData group) {
        if (group.getCount() == 0) {
            group.detach();
        }
    }
}
