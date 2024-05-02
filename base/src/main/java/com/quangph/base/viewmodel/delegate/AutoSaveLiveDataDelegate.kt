package com.quangph.base.viewmodel.delegate

import androidx.lifecycle.MutableLiveData
import com.quangph.base.viewmodel.IStoreData
import com.quangph.base.viewmodel.IStoreDataObserver
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by QuangPH on 2020-11-04.
 */
class AutoSaveLiveDataDelegate<T, TValue>(private val saveStateData: IStoreData,
                                          private val defaultValue: TValue?): ReadWriteProperty<T, MutableLiveData<TValue?>?> {

    private var value: MutableLiveData<TValue?>? = null

    override fun setValue(thisRef: T, property: KProperty<*>, value: MutableLiveData<TValue?>?) {
        //this.value = value
        //saveStateData.putValue(property.name, value?.value)
    }

    override fun getValue(thisRef: T, property: KProperty<*>): MutableLiveData<TValue?>? {
        if (value == null) {
            value = MutableLiveDataWraper(property.name, saveStateData)
            value!!.value = defaultValue
            saveStateData.registerObserver(property.name, object : IStoreDataObserver {
                override fun observe(value: Any?) {
                    this@AutoSaveLiveDataDelegate.value!!.value = value as TValue?
                }
            })
        }

        return value
    }


    class MutableLiveDataWraper<TValue>(private val name: String, private val saveStateData: IStoreData): MutableLiveData<TValue?>() {
        override fun setValue(value: TValue?) {
            super.setValue(value)
            saveStateData.putValue(name, value)
        }
    }
}