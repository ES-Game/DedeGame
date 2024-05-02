package com.quangph.jetpack.view.recyclerview.wrap

import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class LoadMoreGroupAdapter: GroupRclvAdapter(), ILoadmoreAdapter {

    private var isShowLoadMore = false

    private var isLoadMoreSupport = false
    private var onAddItemListener: IOnAddItemListener? = null
    private val loadingGroup = LoadingGroupData()

    override fun addGroup(data: GroupData<*>?) {
        val groupDataSet = groupManager.dataSet
        if (groupDataSet.size == 0) {
            super.addGroup(data)
        } else {
            if (loadingGroup.isAttached) {
                super.addGroupDataAtIndex(groupDataSet.size - 1, data)
            } else {
                super.addGroup(data)
            }
        }
    }

    override fun showLoadMore(isShown: Boolean) {
        if (!isLoadMoreSupport) {
            return
        }

        if (isShown) {
            if (!isShowLoadMore) {
                isShowLoadMore = true
                addGroup(loadingGroup)
                loadingGroup.notifySelfInserted()
            }
        } else {
            if (isShowLoadMore) {
                isShowLoadMore = false
                removeGroup(loadingGroup)
            }
        }
    }

    override fun enableLoadMore(isSupport: Boolean) {
        isLoadMoreSupport = isSupport
    }

    override fun isLoadMoreSupport(): Boolean {
        return isLoadMoreSupport
    }

    override fun isShowingLoadMore(): Boolean {
        return isShowLoadMore
    }

    override fun getLatestPageIndex(): Int {
        val group = findLoadMoreGroupData() ?: return -1
        return group.latestPageIndex
    }

    override fun getTotalCount(): Int {
        val group = findLoadMoreGroupData() ?: return -1
        return group.totalCount
    }

    override fun getLatestPageSize(): Int {
        val group = findLoadMoreGroupData() ?: return -1
        return group.latestPageSize
    }

    override fun setOnAddItemListener(listener: IOnAddItemListener?) {
        onAddItemListener = listener
    }

    override fun addMoreItem(itemList: MutableList<*>?, isStillMore: Boolean) {
        if (itemList == null) return
        if (!isLoadMoreSupport) return
        val group = findLoadMoreGroupData() ?: return
        val oldSize = group.totalCount

        if (itemList.isEmpty()) {
            onAddItemListener?.onAdded(oldSize, 0, group.totalCount)
            showLoadMore(false)
        } else {
            group.addMoreItem(itemList)
            onAddItemListener?.onAdded(oldSize, itemList.size, group.totalCount)
            showLoadMore(isStillMore)
        }
    }

    private fun findLoadMoreGroupData(): IGroupDataLoadMore? {
        for (group in groupManager.dataSet) {
            if (group is IGroupDataLoadMore) {
                return group
            }
        }

        return null
    }
}