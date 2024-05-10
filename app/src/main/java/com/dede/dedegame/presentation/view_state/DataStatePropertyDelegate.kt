package com.example.demo.viewstate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface IUpdatableDataAllocation<V>: ReadWriteProperty<Any, V> {
    fun updateAllocation(newAllocation: HashMap<String, Any?>)
}

open class NullableDataStatePropertyDelegate<V>(private val initValue: V? = null, private val key: String,
                                                private var dataStoreMap: HashMap<String, Any?>)
    : IUpdatableDataAllocation<V?> {

        init {
            if (!dataStoreMap.contains(key) && (initValue != null)) {
                dataStoreMap[key] = initValue
            }
        }

    override fun updateAllocation(newAllocation: HashMap<String, Any?>) {
        this.dataStoreMap = newAllocation
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): V? {
        return if (dataStoreMap.contains(key)) {
            dataStoreMap[key] as V
        } else {
            null
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: V?) {
        if (value == null) {
            dataStoreMap.remove(key)
            return
        }

        val prev = dataStoreMap[key]
        if (prev == null) {
            dataStoreMap[key] = value
        } else {
            val sameClass = value!!::class == prev::class
            if (sameClass) {
                dataStoreMap[key] = value
            } else {
                throw RuntimeException("Previous value is type: ${prev!!::class.qualifiedName}, " +
                        "but you want to set new value with type: ${value!!::class.qualifiedName}")
            }
        }
    }
}

open class DataStatePropertyDelegate<V>(private val initValue: V, private val key: String,
                                        private var dataStoreMap: HashMap<String, Any?>)
    : IUpdatableDataAllocation<V> {

    init {
        if (!dataStoreMap.contains(key)) {
            dataStoreMap[key] = initValue
        }
    }

    override fun updateAllocation(newAllocation: HashMap<String, Any?>) {
        this.dataStoreMap = newAllocation
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): V {
        return dataStoreMap[key] as V
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
        val prev = dataStoreMap[key]
        if (prev == null) {
            dataStoreMap[key] = value
        } else {
            val sameClass = value!!::class == prev::class
            if (sameClass) {
                dataStoreMap[key] = value
            } else {
                throw RuntimeException("Previous value is type: ${prev!!::class.qualifiedName}, " +
                        "but you want to set new value with type: ${value!!::class.qualifiedName}")
            }
        }
    }
}