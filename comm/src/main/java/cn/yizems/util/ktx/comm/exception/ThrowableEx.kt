@file:Suppress("NOTHING_TO_INLINE")

package cn.yizems.util.ktx.comm.exception

import java.io.PrintWriter
import java.io.StringWriter

/**
 * 转换为 日志文本
 */
fun Throwable?.toLogString(): String {
    if (this == null) {
        return ""
    }
    val trace = StringWriter()
    printStackTrace(PrintWriter(trace))
    return trace.toString()
}