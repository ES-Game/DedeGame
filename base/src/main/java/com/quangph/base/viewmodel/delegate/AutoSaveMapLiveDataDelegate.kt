package com.quangph.base.viewmodel.delegate

import com.quangph.base.viewmodel.IStoreData
import com.quangph.base.viewmodel.IStoreDataObserver
import com.quangph.base.viewmodel.livedata.map.MapLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by QuangPH on 2020-11-18.
 */

class AutoSaveMapLiveDataDelegate<T, K, V, MAP : MutableMap<K, V?>>(private val saveStateData: IStoreData,
                                                                    private val defaultValue: MAP?): ReadWriteProperty<T, MapLiveData<K, V?, MAP?>?> {

    private var value: MapLiveData<K, V?, MAP?>? = null

    override fun setValue(thisRef: T, property: KProperty<*>, value: MapLiveData<K, V?, MAP?>?) {

    }

    override fun getValue(thisRef: T, property: KProperty<*>): MapLiveData<K, V?, MAP?>? {
        if (value == null) {
            value = MapLiveDataWraper(property.name, saveStateData)
            value!!.value = defaultValue
            saveStateData.registerObserver(property.name, object : IStoreDataObserver {
                override fun observe(value: Any?) {
                    this@AutoSaveMapLiveDataDelegate.value!!.value = value as MAP?
                }
            })
        }

        return value
    }


    class MapLiveDataWraper<K, V, MAP : Map<K, V>>(private val name: String,
                                                   private val saveStateData: IStoreData): MapLiveData<K, V?, MAP?>() {

        override fun setValue(value: MAP?) {
            super.setValue(value)
            saveStateData.putValue(name, value)
        }
    }
}