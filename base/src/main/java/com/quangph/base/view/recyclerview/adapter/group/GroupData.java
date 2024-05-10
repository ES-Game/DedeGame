package com.quangph.base.view.recyclerview.adapter.group;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder;


/**
 * Created by Pham Hai Quang on 7/27/2019.
 */
public abstract class GroupData<T> {
    public static final int INVALID_RESOURCE = -1;
    public T data;
    public int adapterPosition = -1;
    protected GroupManager mManager;

    public GroupData(T data) {
        this.data = data;
    }

    public void setGroupManager(GroupManager manager) {
        mManager = manager;
    }

    public int getLayoutResource(int viewType) {
        return INVALID_RESOURCE;
    }

    public int getItemViewType(int positionInGroup) {
        return 0;
    }

    public void attach() {
        adapterPosition = mManager.findAdapterPositionForGroup(this);
        Log.i("Adapter position", String.valueOf(adapterPosition));
    }

    public void attachAndNotify() {
        if (!isAttached()) {
            if (getCount() > 0) {
                notifySelfInserted();
            }
        }
    }

    public void detach() {
        adapterPosition = -1;
    }

    public void detachAndNotify() {
        if (isAttached()) {
            //detach();
            int pos = adapterPosition;
            detach();
            mManager.notifyRemove(this, pos, getCount());
        }
    }

    public boolean isAttached() {
        return adapterPosition > -1;
    }

    public int[] getRange() {
        int[] range = new int[2];
        range[0] = adapterPosition;
        range[1] = adapterPosition + getCount();
        return range;
    }

    public boolean containPosition(int adapterPosition) {
        return adapterPosition >= this.adapterPosition && adapterPosition < this.adapterPosition + getCount();
    }

    public void notifyRemove(int groupPosition) {
        if (isAttached()) {
            mManager.notifyRemove(this, adapterPosition + groupPosition);
        }
    }

    public void notifyRemove(int groupPosition, int count) {
        if (isAttached()) {
            mManager.notifyRemove(this, adapterPosition + groupPosition, count);
        }
    }

    public void notifyInserted(int groupPosition, int count) {
        if (count <= 0) {
            return;
        }

        if (!isAttached()) {
            attach();
        }
        mManager.notifyInserted(this, adapterPosition + groupPosition, count);
    }

    public void notifyChanged(int groupPosition) {
        if (!isAttached()) {
            attach();
        }
        mManager.notifyChanged(this, adapterPosition + groupPosition);
    }

    public void notifyChanged(int groupPosition, Object payload) {
        if (!isAttached()) {
            attach();
        }
        mManager.notifyChanged(this, adapterPosition + groupPosition, payload);
    }

    public void notifyChanged(int groupPosition, int count, Object payload) {
        if (!isAttached()) {
            attach();
        }
        mManager.notifyChanged(adapterPosition + groupPosition, count, payload);
    }

    public void notifyChanged() {
        mManager.notifyChanged(this);
    }

    public void notifyDataSetChanged() {
        mManager.notifyDataSetChanged();
    }

    public void notifyChange(Object payload) {
        mManager.notifyChanged(this, payload);
    }

    public void notifySelfInserted() {
        mManager.notityNewGroupAdded(this);
    }

    public void notifySelfRemoved() {
        mManager.removeGroup(this);
    }

    public int mapAdapterPositionToGroupPosition(int adapterPosition) {
        return adapterPosition - this.adapterPosition;
    }

    public int  getAdapterPositionFromGroupPosition(int groupPosition) {
        return this.adapterPosition + groupPosition;
    }

    public void show() {
        attachAndNotify();
    }

    public void hide() {
        detachAndNotify();
    }

    public void gone() {
        mManager.removeGroup(this);
    }

    public void reset(T newData) {
        int oldCount = getCount();
        this.data = newData;
        int newCount = getCount();
        if (oldCount == newCount) {
            this.notifyChanged();
        } else {
            int offset = newCount - oldCount;
            if (offset > 0) {
                //this.notifyChanged(0, oldCount);
                this.notifyChanged(0, oldCount, null);
                this.notifyInserted(oldCount, offset);
            } else {
                this.notifyChanged(0, newCount, null);
                this.notifyRemove(newCount, -offset);
            }
        }
    }

    public abstract Object getDataInGroup(int positionInGroup);

    public abstract int getCount();

    public abstract BaseRclvHolder onCreateVH(View itemView, int viewType);

}
