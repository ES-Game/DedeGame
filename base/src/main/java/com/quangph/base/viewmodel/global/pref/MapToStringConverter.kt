package com.quangph.base.viewmodel.global.pref

import org.json.JSONObject
import java.lang.IllegalArgumentException

class MapToStringConverter(private val objToStringConverter: IPrefConverter): IPrefConverter {

    override fun objToString(obj: Any): String {
        if (obj is Map<*,*>) {
            val map: MutableMap<String, String> = mutableMapOf()
            val itr = obj.keys.iterator()
            while (itr.hasNext()) {
                val next = itr.next() as String
                obj[next]?.let {
                    map[next] = objToStringConverter.objToString(it)
                }
            }
            val json = JSONObject(map.toMap())
            return json.toString()
        } else {
            throw IllegalArgumentException("Just Map<String,*> is allowed")
        }
    }

    override fun strToObj(rawString: String): Any {
        val json = JSONObject(rawString)
        val itr: MutableIterator<String> = json.keys()
        val map: MutableMap<String, Any> = mutableMapOf()
        while (itr.hasNext()) {
            val key = itr.next()
            val valStr: String = json.get(key) as String
            map[key] = objToStringConverter.strToObj(valStr)
        }
        return map
    }
}