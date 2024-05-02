package com.quangph.pattern.behavior

/**
 * Created by QuangPH on 2020-02-08.
 */
open class Behavior {
    private var cycle = 0
    var maxCycle = Int.MAX_VALUE

    /**
     * Return true make this behavior is called in future
     */
    fun execute(): Boolean {
        cycle++
        val result = onExecute()
        val reachToMax = cycle >= maxCycle
        return !reachToMax && result
    }

    open fun onExecute(): Boolean {
        return true
    }
}