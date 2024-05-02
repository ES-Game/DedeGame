package com.quangph.pattern.behavior

/**
 * Created by QuangPH on 2020-02-25.
 */
open class Condition(private val rule: Any, private val locationId: Long? = null) {
    private var andConditionList = mutableListOf<Any>()
    private var orConditionList = mutableListOf<Any>()

    override fun hashCode(): Int {
        return super.hashCode() + javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return if (other is Condition) {
            rule == other.rule
                    && compareTwoList(andConditionList, other.andConditionList)
                    && compareTwoList(orConditionList, other.orConditionList)
        } else {
            false
        }
    }

    open fun match(valid: IRuleValid): Boolean {
        val ruleMatch = valid.match(rule, locationId)
        val andMatch = andCondition(valid)
        val orMatch = orCondition(valid)
        return (ruleMatch && andMatch) || orMatch
    }

    open fun inverse(): Condition {
        return InverseCondition(this)
    }

    fun and(rule: Any): Condition {
        andConditionList.add(rule)
        return this
    }

    fun or(rule: Any): Condition {
        orConditionList.add(rule)
        return this
    }

    private fun andCondition(valid: IRuleValid): Boolean {
        var result = true
        for (rule in andConditionList) {
            if (!valid.match(rule, locationId)) {
                result = false
                break
            }
        }
        return result
    }

    private fun orCondition(valid: IRuleValid): Boolean {
        var result = false
        for (rule in orConditionList) {
            if (valid.match(rule, locationId)) {
                result = true
                break
            }
        }
        return result
    }

    private fun compareTwoList(list1: List<Any>, list2: List<Any>): Boolean {
        if (list1.isEmpty() && list2.isEmpty()) return true
        if (list1.size != list2.size) return false
        if (list1 !== list2) {
            return true
        }
        val areNotEqual = list1.asSequence()
                .map { it in list2 }
                .contains(false)
        return !areNotEqual
    }
}


open class InverseCondition(private val condition: Condition): Condition(Unit) {

    override fun hashCode(): Int {
        return super.hashCode() + javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is InverseCondition) return false
        return condition == other.condition
    }

    override fun match(valid: IRuleValid): Boolean {
        return !condition.match(valid)
    }

    override fun inverse(): Condition {
        return condition
    }
}

class NotCondition(rule: Any): InverseCondition(Condition(rule))