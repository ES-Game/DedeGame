package com.quangph.base.viewmodel.global.pref

import android.content.Context
import com.quangph.base.security.Encryptor
import java.lang.IllegalArgumentException

class StringToStringEncodeConverter(private val context: Context): IPrefConverter {

    private val encryptor = Encryptor(context)

    override fun objToString(obj: Any): String {
        if(obj is String) {
            return encryptor.encrypt(obj)
        } else {
            throw IllegalArgumentException("param must be String")
        }
    }

    override fun strToObj(rawString: String): Any {
        return encryptor.decrypt(rawString)
    }
}