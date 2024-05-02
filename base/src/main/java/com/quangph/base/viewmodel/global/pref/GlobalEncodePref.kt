package com.quangph.base.viewmodel.global.pref

import android.content.Context

class GlobalEncodePref(appContext: Context) : GlobalPref(appContext) {

    private val STRING_TYPE = "_STRING_TYPE____"
    private var strConverter: IPrefConverter? = null

    override fun getValue(name: String): Any? {
        getGlobalShared()?.all?.let {keys ->
            for (entry in keys) {
                if (entry.key.contains(name)) {
                    if (entry.key.contains(STRING_TYPE)) {
                        val value = entry.value ?: return null
                        if (value is String) {
                            initStringConverterIfNeed()
                            return strConverter!!.strToObj(value)
                        }
                    } else {
                        break
                    }
                }
            }
        }
        return super.getValue(name)
    }

    override fun putValue(name: String, value: Any?) {
        if (value == null) {
            super.putValue(name, value)
            return
        }

        if (value is String) {
            val editor = getGlobalShared()?.edit()
            initStringConverterIfNeed()
            editor?.putString(generateKeyForString(name), strConverter!!.objToString(value))
            editor?.commit()
            return
        }

        super.putValue(name, value)
    }

    override fun createObjToStringConverter(): IPrefConverter {
        return ObjToStringEncodedConverter(appContext)
    }

    override fun createMapToStringConverter(): IPrefConverter {
        return MapToStringConverter(ObjToStringEncodedConverter(appContext))
    }

    private fun generateKeyForString(key: String): String {
        return key + STRING_TYPE
    }

    private fun initStringConverterIfNeed() {
        if (strConverter == null) {
            strConverter = StringToStringEncodeConverter(appContext)
        }
    }
}