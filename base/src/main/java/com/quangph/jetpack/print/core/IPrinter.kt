package com.quangph.jetpack.print.core

/**
 * Created by QuangPH on 2020-08-25.
 */
interface IPrinter<T> {
    fun print(data: T, numberOfCopy: Int): Boolean
}