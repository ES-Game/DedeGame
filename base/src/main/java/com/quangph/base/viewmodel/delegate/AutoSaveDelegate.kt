package com.quangph.base.viewmodel.delegate

import com.quangph.base.viewmodel.IStoreData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by QuangPH on 2020-10-22.
 */
class AutoSaveDelegate<T, TValue>(private val saveStateData: IStoreData, defaultValue: TValue?): ReadWriteProperty<T, TValue?> {

    private var value: TValue? = null

    init {
        value = defaultValue
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: TValue?) {
        this.value = value
        saveStateData.putValue(property.name, value)
    }

    override fun getValue(thisRef: T, property: KProperty<*>): TValue? {
        if (value != null) {
            return value
        } else {
            value = saveStateData.getValue(property.name) as TValue?
            return value
        }
    }
}