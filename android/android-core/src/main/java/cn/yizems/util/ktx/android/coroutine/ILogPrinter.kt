package cn.yizems.util.ktx.android.coroutine

import android.util.Log

/**
 * 用于打印日志
 */
fun interface ILogPrinter {
    /**
     * 打印
     * @param tag
     * @param msg
     * @param throwable
     * @param level 级别
     */
    fun printLog(level: Int, tag: String, msg: String, throwable: Throwable?)
}


internal class DefaultLogPrinter : ILogPrinter {
    override fun printLog(level: Int, tag: String, msg: String, throwable: Throwable?) {
        when (level) {
            Log.VERBOSE -> Log.v(tag, msg, throwable)
            Log.DEBUG -> Log.d(tag, msg, throwable)
            Log.INFO -> Log.i(tag, msg, throwable)
            Log.WARN -> Log.w(tag, msg, throwable)
            Log.ERROR -> Log.e(tag, msg, throwable)
            Log.ASSERT -> Log.wtf(tag, msg, throwable)
        }
    }
}
