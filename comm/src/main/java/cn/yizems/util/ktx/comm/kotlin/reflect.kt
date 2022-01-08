package cn.yizems.util.ktx.comm.kotlin

import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

fun <T> KProperty0<T>.isInitialized(): Boolean {
    isAccessible = true
    return (getDelegate() as? Lazy<*>)?.isInitialized() ?: true
}
