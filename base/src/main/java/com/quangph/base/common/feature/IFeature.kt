package com.quangph.base.common.feature

import com.quangph.base.mvp.ICommand

interface IFeature {
    fun match(): Boolean
    fun interceptCommands(): Array<Class<*>>
}