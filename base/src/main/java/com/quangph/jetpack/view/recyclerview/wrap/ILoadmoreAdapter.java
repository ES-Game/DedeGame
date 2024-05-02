package com.quangph.jetpack.view.recyclerview.wrap;

import java.util.List;

/**
 * Created by QuangPH on 2020-03-03.
 */
public interface ILoadmoreAdapter {
    void showLoadMore(boolean isShown);
    void enableLoadMore(boolean isSupport);
    boolean isLoadMoreSupport();
    boolean isShowingLoadMore();
    int getLatestPageIndex();
    int getTotalCount();
    int getLatestPageSize();
    void setOnAddItemListener(IOnAddItemListener listener);
    void addMoreItem(List<?> itemList, boolean isStillMore);
}
