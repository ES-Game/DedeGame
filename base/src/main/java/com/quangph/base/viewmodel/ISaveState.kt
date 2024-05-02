package com.quangph.base.viewmodel

import android.os.Bundle

/**
 * Created by QuangPH on 2020-10-22.
 */
interface ISaveState {
    fun saveInstanceState(outState: Bundle)
    fun restoreInstanceState(savedInstanceState: Bundle)
}