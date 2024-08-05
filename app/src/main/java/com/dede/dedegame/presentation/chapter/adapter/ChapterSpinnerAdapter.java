package com.dede.dedegame.presentation.chapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dede.dedegame.R;
import com.dede.dedegame.domain.model.Chapter;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerInterface;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.List;

public class ChapterSpinnerAdapter extends RecyclerView.Adapter<ChapterSpinnerAdapter.MySpinnerViewHolder> implements PowerSpinnerInterface<Chapter> {
    Context context;
    List<Chapter> list;
    OnSpinnerItemSelectedListener<Chapter> listener;
    int pos;

    public List<Chapter> getItems() {
        return this.list;
    }

    @Override
    public OnSpinnerItemSelectedListener<Chapter> getOnSpinnerItemSelectedListener() {
        return null;
    }

    @Override
    public PowerSpinnerView getSpinnerView() {
        return null;
    }

    @Override
    public void notifyItemSelected(int i) {
    }

    @Override
    public void setItems(@NonNull List<? extends Chapter> list) {
    }

    @Override
    public void setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<Chapter> onSpinnerItemSelectedListener) {
    }

    public ChapterSpinnerAdapter(Context activity, List<Chapter> list, OnSpinnerItemSelectedListener<Chapter> listener) {
        this.context = activity;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MySpinnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySpinnerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dropdown, parent, false));
    }

    @Override
    public void onBindViewHolder(MySpinnerViewHolder holder, final int position) {
        holder.tvChapterName.setText(this.list.get(position).getTitle());
        if (position == this.pos) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(this.context, R.color.orange_300));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(this.context, R.color.white));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(pos, list.get(pos), position, list.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public int getIndex() {
        return this.pos;
    }

    @Override
    public void setIndex(int i) {
        this.pos = i;
    }

    public static class MySpinnerViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterName;

        public MySpinnerViewHolder(View itemView) {
            super(itemView);
            tvChapterName = itemView.findViewById(R.id.tvChapterName);
        }
    }
}
