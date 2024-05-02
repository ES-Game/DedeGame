package com.quangph.pattern.behavior

/**
 * Permission system
 * Created by QuangPH on 2020-02-08.
 */
open class BehaviorAgent {

    lateinit var ruleFactory: RuleFactory
    private val behaviorList = ArrayList<BehaviorInfo>()

    /*fun addBehavior(behavior: Behavior) {
        behaviorList.add(behavior)
    }*/

    fun addBehavior(condition: Condition, behavior: Behavior) {
        addBehavior(condition, behavior, null)
    }

    fun addBehavior(condition: Condition, behavior: Behavior, notBehavior: Behavior?) {
        addToList(behavior, condition)
        if (notBehavior != null) {
            addToList(notBehavior, condition.inverse())
        }
    }

    fun runAll() {
        val valid = ruleFactory.getRuleValid()
        var next = 0
        while (next < behaviorList.size) {
            val behaviorInfo = behaviorList[next]
            if (behaviorInfo.validRule(valid)) {
                if (!behaviorInfo.behavior.execute()) {
                    behaviorList.remove(behaviorInfo)
                    if (behaviorList.size == 0) {
                        break
                    }
                } else {
                    next++
                }
            } else {
                next++
            }
        }
    }

    fun run(condition: Condition) {
        val valid = ruleFactory.getRuleValid()
        var next = 0
        while (next < behaviorList.size) {
            val behaviorInfo = behaviorList[next]
            if (behaviorInfo.match(condition) && behaviorInfo.validRule(valid)) {
                if (!behaviorInfo.behavior.execute()) {
                    behaviorList.remove(behaviorInfo)
                    if (behaviorList.size == 0) {
                        break
                    }
                } else {
                    next++
                }
            } else {
                next++
            }
        }
    }

    fun checkCondition(condition: Condition, executor: () -> Unit, notExecutor: () -> Unit = {}) {
        if (matchCondition(condition)) {
            executor.invoke()
        } else {
            notExecutor.invoke()
        }
    }

    fun matchCondition(condition: Condition): Boolean {
        return condition.match(ruleFactory.getRuleValid())
    }

    private fun addToList(behavior: Behavior, condition: Condition) {
        val info = BehaviorInfo()
        info.behavior = behavior
        info.condition = condition
        behaviorList.add(info)
    }


    inner class BehaviorInfo {
        lateinit var condition: Condition
        lateinit var behavior: Behavior

        fun match(condition: Condition): Boolean {
            return condition == this.condition
        }

        fun validRule(valid: IRuleValid): Boolean {
            return condition.match(valid)
        }
    }
}

