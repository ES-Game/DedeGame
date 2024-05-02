package com.quangph.base.view.recyclerview.adapter;

/**
 * Created by Pham Hai Quang on 1/24/2019.
 */
public class BaseVHData<T> {
    interface IItemStatus {}
    public enum STATUS implements IItemStatus {
        NORMAL, SELECTED, PRESSED, FOCUSED
    }

    public IItemStatus status = STATUS.NORMAL;

    // Type of item
    public int itemType = -1;

    public T realData;

    public BaseVHData() {
        this(null);
    }

    public BaseVHData(T data) {
        this(data, STATUS.NORMAL);
    }

    public BaseVHData(T data, IItemStatus status) {
        this.realData = data;
        this.status = status;
    }
}
