package com.dede.dedegame

import android.content.Context
import android.content.SharedPreferences
import com.dede.dedegame.domain.model.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DedeSharedPref {

    const val DEDE_DATA = "DEDE_DATA"
    const val USER_INFO = "USER_INFO"
    private var sharedPref: SharedPreferences = DedeApp.getInstance().getSharedPreferences(
        DEDE_DATA,
        Context.MODE_PRIVATE
    )

    private fun <T> serializeObject(obj: T): String {
        return Gson().toJson(obj)
    }

    private inline fun <reified T> deserializeObject(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun saveUserInfo(userInfo: UserInfo?) {
        val json = serializeObject(userInfo)
        val editor = sharedPref.edit()
        editor.putString(USER_INFO, json)
        editor.apply()
    }

    fun getUserInfo(): UserInfo? {
        val json = sharedPref.getString(USER_INFO, null)
        return if (json != null) {
            deserializeObject<UserInfo>(json)
        } else {
            null
        }
    }

    fun clearUserInfo() {
        saveUserInfo(null)
    }

}