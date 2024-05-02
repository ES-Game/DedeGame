package com.quangph.pattern.behavior

/**
 * Created by QuangPH on 2020-02-25.
 */
abstract class RuleFactory {
    abstract fun getRuleValid(): IRuleValid
}