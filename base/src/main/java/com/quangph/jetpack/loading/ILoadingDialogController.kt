package com.quangph.jetpack.loading

/**
 * Created by QuangVH on 10/16/2020.
 */
interface ILoadingDialogController {
    fun enable(enable: Boolean)
    fun show()
    fun hide()
    fun show(tag: String)
    fun hide(tag: String)
}