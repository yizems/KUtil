package cn.yizems.util.ktx.android.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.Lifecycle
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Activity 队列管理
 * 要确保一定在主线程运行
 */
@SuppressLint("StaticFieldLeak")
object ActivityStackManager {

    private var initialized = false

    private val list = mutableListOf<Activity>()

    private val listeners = ConcurrentHashMap<Lifecycle.Event, CopyOnWriteArrayList<LifeCallback>>()

    private var curAct: Activity? = null

    fun getCurActivity() = curAct

    fun registerCallback(application: Application) {

        if (initialized) {
            return
        }
        application.registerActivityLifecycleCallbacks(KtActivityLifecycle {

            created = { activity, _ ->
                list.add(activity)
                dispatchListener(Lifecycle.Event.ON_CREATE, activity)
            }

            started = {
                dispatchListener(Lifecycle.Event.ON_START, it)
            }

            resumed = { activity ->
                curAct = activity
                dispatchListener(Lifecycle.Event.ON_RESUME, activity)
            }

            stopped = { activity ->
                if (curAct == activity) {
                    curAct = null
                }
                dispatchListener(Lifecycle.Event.ON_STOP, activity)
            }
            destroyed = { activity ->
                list.remove(activity)
                dispatchListener(Lifecycle.Event.ON_DESTROY, activity)
            }
        })
        initialized = true
    }

    private fun dispatchListener(event: Lifecycle.Event, activity: Activity) {
        listeners[event]?.forEach {
            it.invoke(activity)
        }
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

    /**
     *
     * @param event Event
     * @param block Function1<[@kotlin.ParameterName] Activity, Boolean> return true will remove
     */
    fun addListener(
        event: Lifecycle.Event,
        callback: LifeCallback
    ) {
        val list = listeners[event] ?: CopyOnWriteArrayList()
        listeners[event] = list
        list.add(callback)
    }

    fun removeListener(
        event: Lifecycle.Event,
        callback: LifeCallback
    ) {
        val list = listeners[event] ?: return
        list.remove(callback)
    }

    fun removeListener(
        event: Lifecycle.Event
    ) {
        listeners.remove(event)
    }

    fun interface LifeCallback {
        fun invoke(act: Activity): Boolean
    }
}
