package com.quangph.jetpack.kotlin

/**
 * Created by QuangPH on 2020-03-27.
 */
sealed class ComparatorElement(val larger: Set<ComparatorElement>, val smaller: Set<ComparatorElement>): Comparable<ComparatorElement> {
    constructor(): this(setOf(), setOf())
    constructor(larger: ComparatorElement, smaller: ComparatorElement) : this(setOf(larger), setOf(smaller))

    override fun compareTo(other: ComparatorElement): Int {
        return when (other) {
            in smaller -> -1
            in larger -> 1
            else -> 0
        }
    }
}