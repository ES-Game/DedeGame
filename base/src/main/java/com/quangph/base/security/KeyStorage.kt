package com.quangph.base.security

import android.content.Context

/**
 * Created by QuangPH on 2020-01-16.
 */
object KeyStorage {

    private const val ENCRYPTED_KEY = "ENCRYPTED_KEY"

    fun saveEncryptedKey(context: Context, encryptedKey: String) {
        val pref = context.getSharedPreferences("encrypted.service", Context.MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString(ENCRYPTED_KEY, encryptedKey)
        edit.apply()
    }

    fun getEncryptedKey(context: Context): String? {
        val pref = context.getSharedPreferences("encrypted.service", Context.MODE_PRIVATE)
        return pref.getString(ENCRYPTED_KEY, null)
    }
}