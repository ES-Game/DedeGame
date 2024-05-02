package com.quangph.base.viewmodel.global.pref

import android.content.Context
import com.google.gson.Gson
import com.quangph.base.security.Encryptor

class ObjToStringEncodedConverter(private val context: Context): IPrefConverter {

    private val OBJECT_PREFIX = "_OBJECT_PREFIX____"
    private val encryptor = Encryptor(context)
    private var gson: Gson = Gson()

    override fun objToString(obj: Any): String {
        return prefixOfValue(obj.javaClass) + encryptor.encrypt(gson.toJson(obj))
    }

    override fun strToObj(rawString: String): Any {
        val index = rawString.indexOf(OBJECT_PREFIX)
        val clazzName = rawString.substring(0, index)
        val clazz = Class.forName(clazzName)
        val realStr = rawString.removePrefix(clazzName + OBJECT_PREFIX)
        return gson.fromJson(encryptor.decrypt(realStr), clazz)
    }

    private fun<T> prefixOfValue(clazz: Class<T>): String {
        return clazz.name + OBJECT_PREFIX
    }
}