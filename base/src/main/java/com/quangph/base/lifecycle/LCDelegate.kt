package com.quangph.base.lifecycle

import java.lang.Exception
import java.lang.reflect.Method

/**
 * Lifecycle delegate which will fire lifecycle event to lifecycle observer.
 * Created by Pham Hai QUANG on 9/5/2018.
 */
object LCDelegate {
    private var LIFE_CYCLE_MAP: MutableMap<Any, MutableList<ILifeCycle>> = mutableMapOf()
    private var LIFECYCLE_METHOD_CACHE = mutableMapOf<Class<*>, Method>()

    /**
     * Add lifecycle observer to map
     * @param bindObj which impl lifecycle, are activity or fragment
     * @param lifecycle lifecycle observer which need to be glued to @bindObj
     */
    fun addComponent(bindObj: Any, lifecycle: ILifeCycle) {
        var lcList = LIFE_CYCLE_MAP[bindObj]
        if (lcList == null) {
            lcList = mutableListOf()
            LIFE_CYCLE_MAP[bindObj] = lcList
        }

        if (!lcList.contains(lifecycle)) {
            lcList.add(lifecycle)
        }
    }

    fun removeComponent(bindObj: Any, lifecycle: ILifeCycle) {
        val lcList = LIFE_CYCLE_MAP[bindObj] ?: return
        lcList.remove(lifecycle)
    }

    fun removeComponent(bindObj: Any) {
        LIFE_CYCLE_MAP.remove(bindObj);
    }

    fun clearComponent(bindObj: Any) {
        val lcList = LIFE_CYCLE_MAP[bindObj] ?: return
        lcList.clear();
    }

    /**
     * return true if lifeCycleClass consume event, super class in bindObj cannot be called
     */
    fun <T: ILifeCycle> invoke(bindObj: Any, lifeCycleClass: Class<T>, vararg params: Any): Boolean {
        val lcList = LIFE_CYCLE_MAP[bindObj] ?: return false

        var consume = true
        for (lc in lcList) {
            val result = checkAndCall(lifeCycleClass, lc, *params)
            consume = consume && result
        }

        return consume
    }

    private fun <T: ILifeCycle> checkAndCall(lifeCycleClass: Class<T>, lifeCycleObj: ILifeCycle,
                                             vararg params: Any): Boolean {
        if (lifeCycleClass.isInstance(lifeCycleObj)) {
            var consume = false
            val method = getLifecycleMethod(lifeCycleClass)
            try {
                val result = method.invoke(lifeCycleObj, *params)
                if (result != null && result is Boolean) {
                    consume = result
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return consume
        }
        return false
    }

    private fun <T: ILifeCycle> getLifecycleMethod(lifeCycleClass: Class<T>): Method {
        var method = LIFECYCLE_METHOD_CACHE[lifeCycleClass]
        if (method != null) {
            return method
        }

        val methods = lifeCycleClass.methods
        if (methods == null || methods.size != 1) {
            throw RuntimeException("The life cycle obj named ${lifeCycleClass.simpleName} must has only one method")
        }

        method = methods[0]
        LIFECYCLE_METHOD_CACHE[lifeCycleClass] = method
        return method
    }
}