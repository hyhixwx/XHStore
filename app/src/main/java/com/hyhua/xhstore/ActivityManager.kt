package com.hyhua.xhstore

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference

class ActivityManager private constructor() {

    private val activityRefs = ArrayList<WeakReference<Activity>>() // 防止内存泄漏,TODO 为何能防止内存泄漏
    private val frontBackCallback = ArrayList<FrontBackCallBack>()
    private var activityStartCount = 0
    private var front = true

    companion object {
        val instance: ActivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManager()
        }
    }

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(InnerActivityLifecycleCallbacks())
    }

    inner class InnerActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityRefs.add(WeakReference(activity))
        }

        override fun onActivityStarted(activity: Activity) {
            activityStartCount++
            // activityStartCount > 0，说明应用当前处在可见状态，在前台
            // front = false，说明之前在后台，需置为前台
            if (!front && activityStartCount > 0) {
                front = true
                onFrontBackChanged(front)
            }
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
            activityStartCount--
            if (front && activityStartCount <= 0) {
                front = false
                onFrontBackChanged(front)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            for (activityRef in activityRefs) {
                if (activityRef.get() == activity) {
                    activityRefs.remove(activityRef);
                    break
                }
            }
        }

    }

    private fun onFrontBackChanged(front: Boolean) {
        for (callback in frontBackCallback) {
            callback.onChanged(front)
        }
    }

    val topActivity: Activity?
        get() = if (activityRefs.size <= 0) null else activityRefs[activityRefs.size - 1].get()

    fun addFrontBackCallBack(callBack: FrontBackCallBack) {
        frontBackCallback.add(callBack)
    }

    fun removeFrontBackCallback(callBack: FrontBackCallBack) {
        frontBackCallback.remove(callBack)
    }

    interface FrontBackCallBack {
        fun onChanged(front: Boolean)
    }
}