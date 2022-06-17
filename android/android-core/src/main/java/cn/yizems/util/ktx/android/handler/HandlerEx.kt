package cn.yizems.util.ktx.android.handler

import android.os.Handler
import android.os.Looper
/** 判断当前线程是否是主线程 */
fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()

/** 获取一个懒加载的 主线程的 handler */
val mainHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    Handler(Looper.getMainLooper())
}
