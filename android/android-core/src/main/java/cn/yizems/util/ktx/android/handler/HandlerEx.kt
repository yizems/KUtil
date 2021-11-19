package cn.yizems.util.ktx.android.handler

import android.os.Handler
import android.os.Looper

fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()

val mainHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    Handler(Looper.getMainLooper())
}