package com.quangph.sync

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by QuangVH on 3/5/2021.
 */
object SyncSharedPreference {
    private const val MBH_SYNC_PREF_NAME = "com.quangph.sync"
    private const val SYNC_LATEST_TIME_KEY = "SYNC_LATEST_TIME"
    private const val SYNC_PROGRESS_SUFFIX = "_PROGRESS"
    private const val SYNC_PROGRESS_FORMAT = "%s${SYNC_PROGRESS_SUFFIX}"

    fun getLatestSyncTime(context: Context): Long {
        val pref = context.applicationContext
            .getSharedPreferences(MBH_SYNC_PREF_NAME, Context.MODE_PRIVATE)
        return pref.getLong(SYNC_LATEST_TIME_KEY, -1L)
    }

    fun getSyncProgress(context: Context): Map<String, Float> {
        val pref = context.applicationContext
            .getSharedPreferences(MBH_SYNC_PREF_NAME, Context.MODE_PRIVATE)
        val progressMap = hashMapOf<String, Float>()
        for ((key, value) in pref.all) {
            if (key.endsWith(SYNC_PROGRESS_SUFFIX)) {
                progressMap[key.removeSuffix(SYNC_PROGRESS_SUFFIX)] = value as Float
            }
        }
        return progressMap
    }

    fun setLatestSyncTime(context: Context, syncMillisecond: Long) {
        getEditor(context).putLong(SYNC_LATEST_TIME_KEY, syncMillisecond).apply()
    }

    fun setSyncProgress(context: Context, syncProgressMap: Map<String, Float>) {
        getEditor(context).apply {
            for ((type, progress) in syncProgressMap) {
                putFloat(getProgressKey(type), progress)
            }
        }.apply()
    }

    private fun getEditor(context: Context): SharedPreferences.Editor {
        val pref = context.applicationContext
            .getSharedPreferences(MBH_SYNC_PREF_NAME, Context.MODE_PRIVATE)
        return pref.edit()
    }

    private fun getProgressKey(syncType: String): String {
        return String.format(SYNC_PROGRESS_FORMAT, syncType)
    }
}