package cn.yizems.util.ktx.comm.kotlin

import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/**
 * 反射获取 lazy 属性 是否已经初始化
 */
fun <T> KProperty0<T>.isInitialized(): Boolean {
    isAccessible = true
    return (getDelegate() as? Lazy<*>)?.isInitialized() ?: true
}
