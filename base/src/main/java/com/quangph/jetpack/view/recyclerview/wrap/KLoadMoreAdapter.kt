package com.quangph.jetpack.view.recyclerview.wrap

import android.view.View
import com.quangph.base.R
import com.quangph.base.view.recyclerview.adapter.BaseVHData
import com.quangph.base.view.recyclerview.kt.KRclvAdapter
import com.quangph.base.view.recyclerview.kt.KRclvVHInfo

/**
 * Created by QuangPH on 2020-03-03.
 */
open class KLoadMoreAdapter : KRclvAdapter(), ILoadmoreAdapter {

    companion object {
        const val LOADING_TYPE = -1
    }

    private var onAddItemListener: IOnAddItemListener? = null
    private var isShowLoadMore = false
    private var isLoadMoreSupport = false
    private var latestPageIndex = -1
    private var latestPageSize = 0

    override fun getItemViewType(position: Int): Int {
        /*return if (getItemDataAtPosition(position) is LoadingVHData) {
            LOADING_TYPE
        } else super.getItemViewType(position)*/

        return if (isShowLoadMore) {
            if (position == mDataSet.size - 1) {
                LOADING_TYPE
            } else {
                onGetItemViewType(position)
            }
        } else {
            onGetItemViewType(position)
        }
    }

    override fun createVHInfo(viewType: Int): KRclvVHInfo<*>?{
        if (viewType == LOADING_TYPE) {
            return LoadingVH(R.layout.item_loading)
        }
        return null
    }

    override fun reset(newItems: List<*>?) {
        reset(newItems, false)
    }

    override fun showLoadMore(isShown: Boolean) {
        if (!isLoadMoreSupport) return

        if (isShown) {
            if (!isShowLoadMore) {
                isShowLoadMore = true
                addItemAndNotify(LoadingVHData(null))
            }
        } else {
            if (isShowLoadMore) {
                isShowLoadMore = false
                removeItemAndNotify(mDataSet.size - 1)
            }
        }
    }

    override fun enableLoadMore(isEnable: Boolean) {
        isLoadMoreSupport = isEnable
    }

    override fun isLoadMoreSupport(): Boolean {
        return isLoadMoreSupport
    }

    override fun isShowingLoadMore(): Boolean {
        return isShowLoadMore
    }

    override fun getLatestPageIndex(): Int {
        return latestPageIndex
    }

    override fun getTotalCount(): Int {
        return if(isShowLoadMore) mDataSet.size - 1 else mDataSet.size
    }

    override fun getLatestPageSize(): Int {
        return latestPageSize
    }

    override fun setOnAddItemListener(listener: IOnAddItemListener?) {
        this.onAddItemListener = listener
    }

    override fun addMoreItem(itemList: List<*>, stillMore: Boolean) {
        latestPageIndex++
        latestPageSize = itemList.size
        if (!isLoadMoreSupport) {
            val startIndex = mDataSet.size
            addItemsAndNotify(itemList)
            onAddItemListener?.onAdded(startIndex, itemList.size, mDataSet.size)
            return
        }

        val startIndex = if (isShowLoadMore) mDataSet.size - 1 else mDataSet.size
        if (itemList.isNullOrEmpty()) {
            onAddItemListener?.onAdded(startIndex, 0,
                if (isShowLoadMore) mDataSet.size - 1 else mDataSet.size)
            if (isShowLoadMore) {
                isShowLoadMore = false
                removeItemAndNotify(mDataSet.size - 1)
            }
        } else {
            addItemsAtIndex(itemList, startIndex)
            if (stillMore) {
                if (!isShowLoadMore) {
                    isShowLoadMore = true
                    addItem(LoadingVHData(null))
                    notifyItemRangeInserted(startIndex, itemList.size + 1)
                } else {
                    notifyItemRangeInserted(startIndex, itemList.size)
                }
            } else {
                if (isShowLoadMore) {
                    isShowLoadMore = false
                    notifyItemRangeInserted(startIndex, itemList.size)
                    removeItemAndNotify(mDataSet.size - 1)
                } else {
                    notifyItemRangeInserted(startIndex, itemList.size)
                }
            }
            onAddItemListener?.onAdded(startIndex, itemList.size,
                if (isShowLoadMore) mDataSet.size - 1 else mDataSet.size)
        }
    }

    open fun onGetItemViewType(position: Int): Int {
        return 0
    }

    fun reset(itemList: List<*>?, stillMore: Boolean) {
        latestPageIndex = 0
        latestPageSize = itemList?.size ?: 0
        mDataSet.clear()
        if (itemList != null) {
            mDataSet.addAll(itemList)
        }
        if (isLoadMoreSupport) {
            isShowLoadMore = stillMore
            if (stillMore) {
                mDataSet.add(LoadingVHData(null))
            }
        }

        notifyDataSetChanged()
        onAddItemListener?.onAdded(0, itemList?.size ?: 0, itemList?.size ?: 0)
    }

    /** */
    class LoadingVHData(data: Void?) : BaseVHData<Void?>(data) {
        init {
            itemType = LOADING_TYPE
        }
    }

    internal inner class LoadingVH(layoutId: Int) : KRclvVHInfo<LoadingVHData?>(layoutId) {

        override fun onBind(itemView: View, data: LoadingVHData?) {

        }
    }
}