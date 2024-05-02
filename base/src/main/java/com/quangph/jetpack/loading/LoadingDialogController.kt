package com.quangph.jetpack.loading

import android.content.Context

/**
 * Created by QuangPH on 2020-10-30.
 */
class LoadingDialogController(private val context: Context): ILoadingDialogController {
    private val NO_TAG = "____NOTAG____"

    private var enable: Boolean = false
    private var isShowing = false
    private var loadingDlg: LoadingDialog? = null
    private var showingTagList = mutableListOf<String>()

    override fun enable(enable: Boolean) {
        this.enable = enable
    }

    override fun show() {
        show(NO_TAG)
    }

    override fun show(tag: String) {
        if (!enable) {
            return
        }

        showingTagList.add(tag)
        if (!isShowing) {
            isShowing = true

            if (loadingDlg == null) {
                loadingDlg = LoadingDialog(context)
            }
            loadingDlg?.show()
        }
    }

    override fun hide() {
        hide(NO_TAG)
    }

    override fun hide(tag: String) {
        showingTagList.remove(tag)
        if (isShowing) {
            if (showingTagList.isEmpty()) {
                isShowing = false
                loadingDlg?.dismiss()
            }
        }
    }
}