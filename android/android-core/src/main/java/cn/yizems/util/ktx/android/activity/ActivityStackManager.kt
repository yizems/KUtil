package cn.yizems.util.ktx.android.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application

/**
 * Activity 队列管理
 */
@SuppressLint("StaticFieldLeak")
object ActivityStackManager {

    private var initialized = false

    private val list = mutableListOf<Activity>()

    private var curAct: Activity? = null

    fun getCurActivity() = curAct

    fun registerCallback(application: Application) {
        if (initialized) {
            return
        }
        application.registerActivityLifecycleCallbacks(KtActivityLifecycle {

            created = { activity, _ ->
                list.add(activity)
            }

            resumed = { activity ->
                curAct = activity
            }

            stopped = { activity ->
                if (curAct == activity) {
                    curAct = null
                }
            }
            destroyed = { activity ->
                list.remove(activity)
            }
        })
        initialized = true
    }

    fun exitApp() {
        list.forEach {
            it.finish()
        }
    }

    /**
     * 获取一份 activity list 的 copy
     */
    @Synchronized
    fun getList() = list.toList()
}
