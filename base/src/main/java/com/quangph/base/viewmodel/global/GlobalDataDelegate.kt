package com.quangph.base.viewmodel.global

import com.quangph.base.viewmodel.IStoreData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class GlobalDataDelegate<T, TValue>(private val storage: IStoreData):
    ReadWriteProperty<T,TValue?> {

    private var value: TValue? = null

    override fun getValue(thisRef: T, property: KProperty<*>): TValue? {
        if (value == null) {
            value = storage.getValue(property.name) as? TValue
        }

        return value
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: TValue?) {
        this.value = value
        storage.putValue(property.name, value)
    }
}