package com.dede.dedegame.presentation.widget.customTab;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.dede.dedegame.R;

import java.util.ArrayList;
import java.util.List;

public class CustomTabView extends LinearLayout {

    private Context context;
    private List<TabModel> listData = new ArrayList<>();
    private OnEventListener listener;

    public CustomTabView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        setDisableClick(false);
    }

    public void addItemTab(TabModel tabModel) {
        listData.add(tabModel);
        addOneLayout(tabModel);
    }

    private void addOneLayout(TabModel tabModel) {
        ConstraintLayout containerItem = new ConstraintLayout(context);
        containerItem.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ImageView tabIcon = new ImageView(context);
        tabIcon.setId(R.id.tab_icon);
        ConstraintLayout.LayoutParams iconParams = new ConstraintLayout.LayoutParams(
                getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._45sdp),
                getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._45sdp)
        );
        iconParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        iconParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        iconParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        iconParams.verticalBias = 0.0f;
        tabIcon.setLayoutParams(iconParams);

        containerItem.addView(tabIcon);
        TextView tabText = new TextView(context);
        tabText.setId(R.id.tab_text);
        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.topToBottom = R.id.tab_icon;
        textParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.topMargin = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp);
        tabText.setLayoutParams(textParams);
        tabText.setText(tabModel.name);
        tabText.setTextSize(getResources().getDimension(com.intuit.sdp.R.dimen._5sdp));
        tabText.setTextColor(getResources().getColor(R.color.orange_300));
        tabText.setTypeface(null, Typeface.BOLD);

        if (tabModel.isSelect()) {
            tabIcon.setImageResource(tabModel.resSelect);
            tabText.setTextColor(ContextCompat.getColor(context, R.color.orange_300));
        } else {
            tabIcon.setImageResource(tabModel.resUnselect);
            tabText.setTextColor(ContextCompat.getColor(context, R.color.gray_300));
        }

        containerItem.addView(tabText);
        containerItem.setPadding(getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp),
                getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp),
                getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp),
                getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp));

        containerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disableClick){
                    return;
                }
                int position = indexOfChild(v);
                if (!listData.get(position).isSelect()) {
                    listData.get(position).setSelect(true);
                }
                for (int i = 0; i < listData.size(); i++) {
                    View itemTab = getChildAt(i);
                    if (i == position) {
                        ImageView tabIcon = itemTab.findViewById(R.id.tab_icon);
                        TextView tabText = itemTab.findViewById(R.id.tab_text);
                        tabIcon.setImageResource(listData.get(i).resSelect);
                        tabText.setTextColor(ContextCompat.getColor(context, R.color.orange_300));
                    } else {
                        listData.get(i).setSelect(false);
                        ImageView tabIcon = itemTab.findViewById(R.id.tab_icon);
                        TextView tabText = itemTab.findViewById(R.id.tab_text);
                        tabIcon.setImageResource(listData.get(i).resUnselect);
                        tabText.setTextColor(ContextCompat.getColor(context, R.color.gray_300));
                    }
                }
                listener.onItemClick(listData.get(position), position);
            }
        });
        addView(containerItem);
    }

    private Boolean disableClick;

    public void setDisableClick(Boolean disableClick) {
        this.disableClick = disableClick;
    }

    public Boolean getDisableClick() {
        return disableClick;
    }

    public void setEvenListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        void onItemClick(TabModel tabModel, int pos);
    }

}
