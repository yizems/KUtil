@file:Suppress("NOTHING_TO_INLINE")

package cn.yizems.util.ktx.comm.type

import cn.yizems.util.ktx.comm.type.BooleanType.FALSE
import cn.yizems.util.ktx.comm.type.BooleanType.TRUE
import kotlin.reflect.KMutableProperty0

/**
 * true 时 执行 block
 *
 * @return self
 */
inline fun Boolean?.onTrue(block: () -> Unit): Boolean? {
    if (this == true) {
        block()
    }
    return this
}

/**
 * false 时 执行 block
 *
 * @return self
 */
inline fun Boolean?.onFalse(block: () -> Unit): Boolean? {
    if (this == false) {
        block()
    }
    return this
}

/**
 * null 时 执行 block
 *
 * @return self
 */
inline fun Boolean?.onNull(block: () -> Unit): Boolean? {
    if (this == null) {
        block()
    }
    return this
}

/**
 * 非 true 时执行block
 *
 * @return self
 */
inline fun Boolean?.onNotTure(block: (Boolean?) -> Unit): Boolean? {
    if (this != true) {
        block(this)
    }
    return this
}

/**
 * 非 false 时执行block
 *
 * @return self
 */
inline fun Boolean?.onNotFalse(block: (Boolean?) -> Unit): Boolean? {
    if (this != false) {
        block(this)
    }
    return this
}

/**
 * null 当做 true
 */
inline fun Boolean?.nullAsTrue(): Boolean {
    return this ?: true
}

/**
 * null 当做 false
 */
inline fun Boolean?.nullAsFalse(): Boolean {
    return this ?: true
}

@Deprecated("Use toBooleanEx() instead", ReplaceWith("toBooleanEx()"))
fun Any?.toBoolean(): Boolean {
    return this.toBooleanEx()
}

@Deprecated("Use toBooleanExNullable() instead", ReplaceWith("toBooleanExNullable()"))
fun Any?.toBooleanNullable(): Boolean? {
    return this.toBooleanExNullable()
}


/**
 * 转为 [Boolean], 兼容 [String] 和 [Number]
 *
 * [String] 支持的值: "true", "1", "0","yes","on",认为是true
 * null as false
 * @return Boolean
 */
fun Any?.toBooleanEx(): Boolean {
    return this.toBooleanExNullable() ?: false
}

/**
 * null as null
 * @see toBooleanEx
 */
fun Any?.toBooleanExNullable(): Boolean? {
    this ?: return null

    return when (this) {
        is CharSequence -> this == "true" || this == "1" || this == "yes" || this == "y" || this == "on" || this == "1.0"
        is Number -> this.toInt() == 1
        is Boolean -> this
        else -> false
    }
}

/**
 * 设置 boolean 类型变量
 *
 * example:
 * ```
 *  XXX::xxx.setBooleanEx(BooleanType)
 * ```
 *
 * @receiver KMutableProperty0<T>
 * @param type BooleanType
 */
inline fun <reified T> KMutableProperty0<T>.setBooleanEx(type: BooleanType) {
    when (returnType.classifier) {
        Boolean::class -> set((type.positive) as T)
        Int::class -> (if (type.positive) 1 else 0) as T
        Double::class -> set((if (type.positive) 1.0 else 0.0) as T)
        String::class -> set(type.value as T)
    }
}


/**
 * 类型
 * String 类型, 给的是字符串, [TRUE] [FALSE] 给的是 0,1,其他是对应字符串
 * Int double 给的是数值
 */
enum class BooleanType(val value: String, val positive: Boolean) {
    TRUE("1", true),
    FALSE("0", false),
    TRUE_STRING("true", true),
    FALSE_STRING("false", false),
    YES("yes", true),
    NO("no", false),
    ON("on", true),
    OFF("off", false),
}


