package com.quangph.base.viewmodel.global

import com.quangph.base.viewmodel.IStoreData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty1

open class GlobalData(private val storage: IStoreData) {

    fun<T> autoSave(): ReadWriteProperty<Any, T?> {
        return GlobalDataDelegate(storage)
    }

    fun update(name: String) {
        val property = this::class.members.first { it.name == name } as? KProperty1<Any, *>
        storage.putValue(name, property?.get(this))
    }
}