package com.quangph.base.view.recyclerview.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Pham Hai Quang on 1/11/2019.
 */
public abstract class BaseRclvAdapter extends RecyclerView.Adapter<BaseRclvHolder> {

    private SparseArray<Stack<BaseRclvHolder>> mCacheViewHolder = new SparseArray<Stack<BaseRclvHolder>>();
    private AsyncLayoutInflater mAsyncInflater;

    public List mDataSet = new ArrayList<>();

    public BaseRclvAdapter(){}

    public BaseRclvAdapter(Context context){
        mAsyncInflater = new AsyncLayoutInflater(context);
    }

    @NonNull
    @Override
    public BaseRclvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Stack<BaseRclvHolder> stack = mCacheViewHolder.get(viewType);
        if (stack != null && !stack.isEmpty()) {
            return stack.pop();
        } else {
            int layout = getLayoutResource(viewType);
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
            return onCreateVH(v, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRclvHolder baseRclvHolder, int position) {
        baseRclvHolder.onBind(getItemDataAtPosition(position));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRclvHolder baseRclvHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(baseRclvHolder, position, payloads);
        } else {
            baseRclvHolder.onBind(getItemDataAtPosition(position), payloads);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public Object getItemDataAtPosition(int position) {
        return mDataSet.get(position);
    }

    public abstract int getLayoutResource(int viewType);
    public abstract BaseRclvHolder onCreateVH(View itemView, int viewType);

    public void onPreInflate(final int viewType, int count, final RecyclerView.LayoutParams params) {
        final Stack<BaseRclvHolder> stack = new Stack<>();
        for (int i = 0; i < count; i++) {
            mAsyncInflater.inflate(getLayoutResource(viewType), null, new AsyncLayoutInflater.OnInflateFinishedListener() {
                @Override
                public void onInflateFinished(@NonNull View view, int i, @Nullable ViewGroup viewGroup) {
                    RecyclerView.LayoutParams p = new RecyclerView.LayoutParams(params);
                    view.setLayoutParams(p);
                    stack.push(onCreateVH(view, viewType));
                }
            });
        }
        mCacheViewHolder.put(viewType, stack);
    }

    public void addItem(Object item) {
        mDataSet.add(item);
    }

    public void addItems(List items) {
        mDataSet.addAll(items);
    }

    public void addItemAndNotify(Object item) {
        mDataSet.add(item);
        notifyItemInserted(mDataSet.size() - 1);
    }

    public void addItemAtIndexAndNotify(int index, Object item) {
        mDataSet.add(index, item);
        notifyItemInserted(index);
    }

    public void addItemsAtIndex(List items, int index) {
        mDataSet.addAll(index, items);
    }

    public void addItemAtIndex(Object item, int index) {
        mDataSet.add(index, item);
    }

    public void addItemsAndNotify(List items) {
        int start = mDataSet.size();
        mDataSet.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }

    public void removeItemAndNotify(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }

    public void reset(List newItems) {
        mDataSet.clear();
        addItems(newItems);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }

    public void clearData() {
        mDataSet.clear();
    }
}
