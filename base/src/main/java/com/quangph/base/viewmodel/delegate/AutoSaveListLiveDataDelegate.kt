package com.quangph.base.viewmodel.delegate

import com.quangph.base.viewmodel.IStoreData
import com.quangph.base.viewmodel.IStoreDataObserver
import com.quangph.base.viewmodel.livedata.collection.ListLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by QuangPH on 2020-11-17.
 */
class AutoSaveListLiveDataDelegate<T, E>(private val saveStateData: IStoreData,
                                         private val defaultValue: ArrayList<E?>?): ReadWriteProperty<T, ListLiveData<E?>?> {

    private var value: ListLiveData<E?>? = null

    override fun setValue(
        thisRef: T,
        property: KProperty<*>,
        value: ListLiveData<E?>?) {

    }

    override fun getValue(thisRef: T, property: KProperty<*>): ListLiveData<E?>? {
        if (value == null) {
            value =
                ListLiveDataWrapper(
                    property.name,
                    saveStateData
                )
            value!!.value = defaultValue
            saveStateData.registerObserver(property.name, object : IStoreDataObserver {
                override fun observe(storedValue: Any?) {
                    this@AutoSaveListLiveDataDelegate.value!!.value = storedValue as MutableList<E?>?
                }
            })
        }

        return value
    }


    private class ListLiveDataWrapper<E>(private val name: String, private val saveStateData: IStoreData): ListLiveData<E?>() {
        override fun setValue(value: MutableList<E?>?) {
            super.setValue(value)
            saveStateData.putValue(name, value)
        }
    }
}