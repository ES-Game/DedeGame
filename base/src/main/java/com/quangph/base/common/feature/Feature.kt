package com.quangph.base.common.feature

import com.quangph.base.mvp.IParentPresenter
import com.quangph.base.mvp.IView
import com.quangph.base.mvp.mvpcomponent.SimplePresenter

abstract class Feature<V: IView>(parent: IParentPresenter?) : SimplePresenter<V>(parent), IFeature {

    private var isMatch: Boolean = false

    init {
        isMatch = match()
    }

    override fun ready() {
        if (isMatch) {
            super.ready()
        }
    }

    override fun interceptCommands(): Array<Class<*>> {
        return if (isMatch) {
            onInterceptCommands()
        } else {
            arrayOf()
        }
    }

    open fun onInterceptCommands(): Array<Class<*>> {
        return arrayOf()
    }
}