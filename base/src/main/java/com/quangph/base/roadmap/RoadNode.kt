package com.quangph.base.roadmap

import android.os.Parcel
import android.os.Parcelable
import com.quangph.pattern.node.LineNode

/**
 * Created by QuangPH on 2020-11-30.
 */
class RoadNode() : LineNode<String>(null), Parcelable {
    var screenName: String? = null
    var isAvailable: Boolean = true

    constructor(parcel: Parcel) : this() {
        screenName = parcel.readString()
        isAvailable = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(screenName)
        parcel.writeByte(if (isAvailable) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoadNode> {
        override fun createFromParcel(parcel: Parcel): RoadNode {
            return RoadNode(parcel)
        }

        override fun newArray(size: Int): Array<RoadNode?> {
            return arrayOfNulls(size)
        }
    }
}