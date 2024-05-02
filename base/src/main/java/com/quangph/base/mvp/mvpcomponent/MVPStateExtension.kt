package com.quangph.base.mvp.mvpcomponent

import android.os.Bundle

/**
 * Created by QuangPH on 2020-02-13.
 */

fun Bundle.buildResult(builder: (Bundle) -> Unit) {
    builder(this)
}