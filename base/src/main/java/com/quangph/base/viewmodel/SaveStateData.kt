package com.quangph.base.viewmodel

import android.os.Bundle
import android.os.Parcelable


/**
 * Created by QuangPH on 2020-10-22.
 */
class SaveStateData : ISaveState, IStoreData {

    private val MARK_IS_MAP = "is_map"

    private val map = HashMap<String, Any?>()
    private val observerMap = HashMap<String, IStoreDataObserver>()

    override fun saveInstanceState(outState: Bundle) {
        map.forEach { (key, value) ->
            save(outState, key, value)
        }
    }

    override fun restoreInstanceState(savedInstanceState: Bundle) {
        val keySet = savedInstanceState.keySet()
        keySet.forEach {
            restore(savedInstanceState, it)
        }

        observerMap.forEach{
            it.value.observe(map[it.key])
        }
    }

    override fun registerObserver(key: String, observer: IStoreDataObserver) {
        observerMap[key] = observer
    }

    override fun putValue(name: String, value: Any?) {
        map.put(name, value)
    }

    override fun getValue(name: String): Any? {
        return map[name]
    }

    private fun save(outState: Bundle, key: String, value: Any?) {
        if (value is String) {
            outState.putString(key, value)
        } else if (value is Int) {
            outState.putInt(key, value)
        } else if (value is Long) {
            outState.putLong(key, value)
        } else if (value is Double) {
            outState.putDouble(key, value)
        } else if (value is Float) {
            outState.putFloat(key, value)
        } else if (value is Boolean) {
            outState.putBoolean(key, value)
        } else if (value is Parcelable) {
            outState.putParcelable(key, value)
        } else if (value is ArrayList<*>) {
            outState.putParcelableArrayList(key, value as ArrayList<out Parcelable>?)
        } else if (value is Map<*, *>) {
            writeMap(outState, key, value)
        }
    }

    private fun writeMap(outState: Bundle, key: String, value: Map<*, *>?) {
        if (value == null) return
        val bundle = Bundle()
        bundle.putBoolean(MARK_IS_MAP, true)
        val allKeys = arrayListOf<Any?>()
        val allValues = arrayListOf<Any?>()
        for ((key1, value1) in value.entries) {
            allKeys.add(key1)
            allValues.add(value1)
        }

        save(bundle, createBundleKeyForMapKey(key), allKeys)
        save(bundle, createBundleKeyForMapValue(key), allValues)

        outState.putBundle(key, bundle)
    }

    private fun restore(savedInstanceState: Bundle, key: String) {
        val value = savedInstanceState.get(key)
        if (value is Bundle) {
            if (value.getBoolean(MARK_IS_MAP)) {
                map[key] = readMap(key, value)
            }
        } else {
            map[key] = value
        }
    }

    private fun readMap(key: String, savedState: Bundle): Map<Any?, Any?>? {
        val allKeys: ArrayList<Any?>? = savedState.getParcelableArrayList<Parcelable>(createBundleKeyForMapKey(key)) as ArrayList<Any?>?
        val allValues: ArrayList<Any?>? = savedState.getParcelableArrayList<Parcelable>(createBundleKeyForMapValue(key)) as ArrayList<Any?>?
        if (!allKeys.isNullOrEmpty() && !allValues.isNullOrEmpty()) {
            val map = hashMapOf<Any?, Any?>()
            for (i in 0 until allKeys.size) {
                map[allKeys[i]] = allValues[i]
            }
            return map
        }
        return null
    }

    private fun createBundleKeyForMapKey(mapName: String): String {
        return mapName + "_map_key"
    }

    private fun createBundleKeyForMapValue(mapName: String): String {
        return mapName + "_map_value"
    }
}