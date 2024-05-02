package com.quangph.base.viewmodel.global.pref

interface IPrefConverter {
    fun objToString(obj: Any): String
    fun strToObj(rawString: String): Any
}