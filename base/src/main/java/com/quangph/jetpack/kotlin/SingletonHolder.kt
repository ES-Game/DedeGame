package com.quangph.jetpack.kotlin

/**
 * Created by QuangPH on 2020-02-03.
 */
open class SingletonHolder<out T : Any, in A: Any>(creator: (A) -> T) {
    private val creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        if (instance != null) {
            return instance!!
        }

        return synchronized(this) {
            if (instance != null) {
                instance
            } else {
                instance = creator!!(arg)
            }
            instance!!
        }
    }
}


class Test private constructor(val name: String) {
    companion object: SingletonHolder<Test, String>(::Test)

    fun call() {
        //logd(name)
    }
}

class Client() {
    init {
        Test.getInstance("abc").call()
    }
}