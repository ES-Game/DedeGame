package com.dede.dedegame.extension.parcel

import android.os.Parcelable

interface KParcelable : Parcelable, Cloneable {
    override fun describeContents(): Int {
        return 0
    }

    public override fun clone(): Any {
        return super.clone()
    }
}