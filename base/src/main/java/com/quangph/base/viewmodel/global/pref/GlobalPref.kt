package com.quangph.base.viewmodel.global.pref

import android.content.Context
import android.content.SharedPreferences
import com.quangph.base.viewmodel.IStoreData
import com.quangph.base.viewmodel.IStoreDataObserver

open class GlobalPref(val appContext: Context): IStoreData {

    private val MAP_TYPE = "_MAP_TYPE____"
    private val SET_TYPE = "_SET_TYPE____"
    private val OBJ_TYPE = "_OBJ_TYPE____"
    private var sharedPref: SharedPreferences? = null
    private var objToStringConverter: IPrefConverter? = null
    private var mapToStringConverter: IPrefConverter? = null

    init {
        sharedPref = appContext.getSharedPreferences("global_cache", Context.MODE_PRIVATE)
    }

    override fun putValue(name: String, value: Any?) {
        if (value == null) {
            sharedPref?.all?.let { keys ->
                for (entry in keys) {
                    if ((entry.key != name) && (!entry.key.contains(name))) continue
                    sharedPref?.edit()?.remove(entry.key)?.commit()
                    break
                }
            }
            return
        }

        val editor = sharedPref?.edit()
        if (value is Boolean) {
            editor?.putBoolean(name, value)
        } else if (value is Int) {
            editor?.putInt(name, value)
        } else if (value is Float) {
            editor?.putFloat(name, value)
        } else if (value is Long) {
            editor?.putLong(name, value)
        } else if (value is String) {
            editor?.putString(name, value)
        } else if (value is List<*>) {
            initObjToStringConverterIfNeed()
            val set: HashSet<String> = hashSetOf()
            for (obj in value) {
                if (obj != null) {
                    set.add(objToStringConverter!!.objToString(obj))
                }
            }
            editor?.putStringSet(generateKeyForSet(name), set)
        } else if (value is Map<*,*>) {
            initMapToStringConverterIfNeed()
            editor?.putString(generateKeyForMap(name), mapToStringConverter!!.objToString(value))
        } else {
            initObjToStringConverterIfNeed()
            editor?.putString(generateKeyForObj(name), objToStringConverter!!.objToString(value))
        }
        editor?.commit()
    }

    override fun getValue(name: String): Any? {
        sharedPref?.all?.let { keys ->
            for (entry in keys) {
                if ((entry.key != name) && (!entry.key.contains(name))) continue
                val value = entry.value ?: return null
                if (entry.key.contains(OBJ_TYPE)) {
                    if (value is String) {
                        initObjToStringConverterIfNeed()
                        return objToStringConverter!!.strToObj(value)
                    }
                }

                if (entry.key.contains(SET_TYPE)) {
                    if (value is Set<*>) {
                        return extractListObj(value as Set<String>)
                    }
                }

                if (entry.key.contains(MAP_TYPE)) {
                    if (value is String) {
                        initMapToStringConverterIfNeed()
                        return mapToStringConverter!!.strToObj(value)
                    }
                }

                return entry.value
            }
        }

        return null
    }

    override fun registerObserver(key: String, observer: IStoreDataObserver) {

    }

    open fun createObjToStringConverter(): IPrefConverter {
        return ObjToStringConverter()
    }

    open fun createMapToStringConverter(): IPrefConverter {
        initObjToStringConverterIfNeed()
        return MapToStringConverter(objToStringConverter!!)
    }

    fun getGlobalShared(): SharedPreferences? {
        return sharedPref
    }

    private fun initObjToStringConverterIfNeed() {
        if (objToStringConverter == null) {
            objToStringConverter = createObjToStringConverter()
        }
    }

    private fun initMapToStringConverterIfNeed() {
        if (mapToStringConverter == null) {
            mapToStringConverter = createMapToStringConverter()
        }
    }

    private fun generateKeyForObj(key: String): String {
        return key + OBJ_TYPE
    }

    private fun generateKeyForSet(key: String): String {
        return key + SET_TYPE
    }

    private fun generateKeyForMap(key: String): String {
        return key + MAP_TYPE
    }

    private fun extractListObj(set: Set<String>): List<Any> {
        initObjToStringConverterIfNeed()
        val itr = set.iterator()
        val result: MutableList<Any> = mutableListOf()
        while (itr.hasNext()) {
            val next = itr.next()
            result.add(objToStringConverter!!.strToObj(next))
        }
        return result
    }
}