package com.quangph.jetpack.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Created by QuangPH on 2020-11-30.
 */
class FragmentNaviSource(private val fragment: Fragment): INaviSource {
    override fun getContext(): Context? {
        return fragment.activity
    }

    override fun startActivity(intent: Intent) {
        fragment.startActivity(intent)
    }

    override fun startActivityForResult(requestCode: Int, intent: Intent) {
        fragment.startActivityForResult(intent, requestCode)
    }
}