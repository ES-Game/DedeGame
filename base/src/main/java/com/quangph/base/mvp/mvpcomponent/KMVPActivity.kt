package com.quangph.base.mvp.mvpcomponent

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.quangph.base.mvp.IView
import com.quangph.base.viewmodel.SaveStateData
import com.quangph.base.viewmodel.delegate.AutoSaveDelegate
import com.quangph.base.viewmodel.delegate.AutoSaveListLiveDataDelegate
import com.quangph.base.viewmodel.delegate.AutoSaveLiveDataDelegate
import com.quangph.base.viewmodel.delegate.AutoSaveMapLiveDataDelegate
import com.quangph.base.viewmodel.livedata.collection.ListLiveData
import com.quangph.base.viewmodel.livedata.map.MapLiveData
import kotlin.properties.ReadWriteProperty

/**
 * Created by QuangPH on 2020-11-24.
 */
open class KMVPActivity<V: IView>: MVPActivity<V>() {

    private val saveStateData = SaveStateData()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveStateData.saveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (!checkOrientation()) {
            saveStateData.restoreInstanceState(savedInstanceState)
        }
    }

    fun<TValue> autoSaveLiveData(defaultValue: TValue? = null): ReadWriteProperty<Any, MutableLiveData<TValue?>?> {
        return AutoSaveLiveDataDelegate(
            saveStateData,
            defaultValue
        )
    }

    fun<TValue> autoSave(defaultValue: TValue? = null): ReadWriteProperty<Any, TValue?> {
        return AutoSaveDelegate(saveStateData, defaultValue)
    }

    fun<E> autoSaveListLiveData(defaultValue: ArrayList<E?>? = null): ReadWriteProperty<Any, ListLiveData<E?>?> {
        return AutoSaveListLiveDataDelegate(saveStateData, defaultValue)
    }

    fun <K,V> autoSaveMapLiveData(defaultValue: MutableMap<K, V?>? = null) : ReadWriteProperty<Any, MapLiveData<K, V?, MutableMap<K, V?>?>?> {
        return AutoSaveMapLiveDataDelegate(saveStateData, defaultValue)
    }
}