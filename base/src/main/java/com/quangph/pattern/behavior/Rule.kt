package com.quangph.pattern.behavior

/**
 * Created by QuangPH on 2020-02-24.
 */

interface IRuleValid {
    fun match(rule: Any, locationId: Long?): Boolean
}