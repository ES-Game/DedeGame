package com.quangph.jetpack

import android.app.Activity
import android.os.Bundle
import com.quangph.base.common.BaseMultiDexApplication
import java.util.*

class JetMultiDexApplication: BaseMultiDexApplication() {
    private val activityStack = Stack<IJetStackable>()

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(StackJetContextLCCallback())
    }

    fun getActivityStack(): Stack<IJetStackable> {
        return activityStack
    }


    /**********************************************************************************************/
    private inner class StackJetContextLCCallback: ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity is IJetStackable) {
                if (activity.allowPushToStack()) {
                    activityStack.push(activity)
                }
            }
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity is IJetStackable) {
                if (activity.allowPushToStack()) {
                    activityStack.remove(activity)
                }
            }
        }
    }
}