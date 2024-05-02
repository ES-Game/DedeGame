package com.quangph.jetpack.view.recyclerview.wrap;

import java.util.List;

public interface IGroupDataLoadMore {
    int getLatestPageIndex();
    int getTotalCount();
    int getLatestPageSize();
    void addMoreItem(List<?> itemList);
}
