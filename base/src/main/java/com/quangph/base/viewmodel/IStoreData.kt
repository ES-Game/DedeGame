package com.quangph.base.viewmodel

/**
 * Created by QuangPH on 2020-11-06.
 */
interface IStoreData {
    fun putValue(name: String, value: Any?)
    fun getValue(name: String): Any?
    fun registerObserver(key: String, observer: IStoreDataObserver)
}