package com.quangph.base.view.recyclerview.adapter.group


abstract class ListGroupData<E>(data: MutableList<E>?) : GroupData<MutableList<E>>(data) {
    override fun getDataInGroup(positionInGroup: Int): Any? {
        return if (data != null) {
            data[positionInGroup]
        } else {
            null
        }
    }

    override fun getCount(): Int {
        return if (data == null) {
            0
        } else {
            data.size
        }
    }

    override fun reset(newData: MutableList<E>?) {
        if (newData.isNullOrEmpty()) {
            return
        }

        if (!isAttached) {
            this.data = mutableListOf()
            this.data.addAll(newData)
            attachAndNotify()
        } else {
            val lastPos = this.data?.size ?: 0
            val lastCount = this.data?.size ?: 0
            this.data.clear()
            this.data.addAll(newData)
            val delta = this.data.size - lastCount
            notifyChanged()
            if (delta > 0) {
                notifyInserted(lastPos, delta)
            } else if (delta < 0) {
                notifyRemove(lastCount, -delta)
            } else {
                notifyChanged()
            }
        }
    }

    fun addItems(items: List<E>) {
        if (!isAttached) {
            if (this.data.isNullOrEmpty()) {
                this.data = mutableListOf()
            }
            this.data.addAll(items)
            attachAndNotify()
        } else {
            val lastPos = this.data?.size ?: 0
            val lastCount = this.data?.size ?: 0
            this.data.addAll(items)
            val delta = this.data.size - lastCount

            if (delta > 0) {
                notifyInserted(lastPos, delta)
            } else {
                notifyRemove(lastCount, -delta)
            }
        }
    }
}