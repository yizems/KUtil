package cn.yizems.util.ktx.android.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * 绑定于 lifeCycle 自动取消协程
 */
fun CoroutineContext.bindLifeCycle(lifecycle: Lifecycle): CoroutineContext {
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            this@bindLifeCycle.cancel()
        }
    })
    return this
}
