package com.quangph.jetpack.loading

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.quangph.base.R

/**
 * Created by QuangPH on 2020-10-30.
 */
class LoadingDialog(private val act: Context) : AppCompatDialog(act) {

    var config: LoadingDialogConfig = LoadingDialogConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCanceledOnTouchOutside(false)
        setOnDismissListener {  }
    }

    override fun onBackPressed() {
        if (config.finishWhenCancelLoading()) {
            dismiss()
            (act as Activity).finish()
        } else {
            super.onBackPressed()
        }
    }
}