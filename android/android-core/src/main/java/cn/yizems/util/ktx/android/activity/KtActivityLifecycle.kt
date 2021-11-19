package cn.yizems.util.ktx.android.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * 简化生命周期回调
 * Created by YZL on 2018/3/9.
 */
class KtActivityLifecycle(init: (KtActivityLifecycle.() -> Unit)? = null) :
    Application.ActivityLifecycleCallbacks {
    init {
        init?.invoke(this)
    }

    var paused: ((Activity) -> Unit)? = null
    var resumed: ((Activity) -> Unit)? = null
    var started: ((Activity) -> Unit)? = null
    var destroyed: ((Activity) -> Unit)? = null
    var saveInstanceState: ((activity: Activity, outState: Bundle?) -> Unit)? = null
    var stopped: ((Activity) -> Unit)? = null
    var created: ((activity: Activity, savedInstanceState: Bundle?) -> Unit)? = null

    override fun onActivityPaused(activity: Activity) {
        paused?.invoke(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        resumed?.invoke(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        started?.invoke(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        destroyed?.invoke(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        saveInstanceState?.invoke(activity, outState)
    }

    override fun onActivityStopped(activity: Activity) {
        stopped?.invoke(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        created?.invoke(activity, savedInstanceState)
    }

}