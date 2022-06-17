@file:Suppress("UNCHECKED_CAST")

package cn.yizems.util.ktx.comm.type

import cn.yizems.util.ktx.comm.number.formatStr
import java.util.*

/**
 * null 时 返回 [other]
 */
infix fun <T : CharSequence?> CharSequence?.nullOr(other: T): T {
    if (this == null) {
        return other
    }
    return this.toString() as T
}

/**
 * null 时 返回 [others] 中不为null的值,都为 null 则返回 null
 */
fun <T : CharSequence?> CharSequence?.nullOr(vararg others: T): T {
    if (this != null) {
        return this as T
    }
    others.forEach {
        if (it != null) {
            return it
        }
    }
    return others.last()
}

/**
 * empty 时 返回 [other]
 */
infix fun <T : CharSequence?> CharSequence?.emptyOr(other: T): T {
    if (this.isNullOrEmpty()) {
        return other
    }
    return this.toString() as T
}

/**
 * empty 时 返回 [others] 不为空的值
 */
fun <T : CharSequence?> CharSequence?.emptyOr(vararg others: T): T {
    if (!this.isNullOrEmpty()) {
        return this as T
    }
    others.forEach {
        if (!it.isNullOrEmpty()) {
            return it
        }
    }
    return others.last()
}

/**
 * blank 时返回 [other]
 */
infix fun <T : CharSequence?> CharSequence?.blankOr(other: T): T {
    if (this.isNullOrBlank()) {
        return other
    }
    return this.toString() as T
}

/** blank 时 返回 [others] 不为空的值 */
fun <T : CharSequence?> CharSequence?.blankOr(vararg others: T): T {
    if (!this.isNullOrBlank()) {
        return this as T
    }
    others.forEach {
        if (!it.isNullOrBlank()) {
            return it
        }
    }
    return others.last()
}

/**
 * 删除结尾 .000
 * :2018-02-02 12:00:00.000
 * @receiver String
 * @param default String
 * @return String
 */
fun String?.dropDatePoint(): String {
    if (this.isNullOrBlank()) {
        return ""
    }
    if (this.contains(".")) {
        return this.split(".")[0]
    }
    return this
}

/** 转为double时, null->null, '.'->0.0 */
fun String?.toDoubleEx(): Double? {
    if (this.isNullOrBlank()) {
        return null
    }
    if (this == ".") {
        return 0.0
    }
    return this.toDouble()
}

/**
 * 转为double,如果出错或为null , 返回 [default]
 */
fun String?.toDoubleOrElse(default: Double = 0.0, ignoreException: Boolean = true): Double {
    return if (ignoreException) {
        try {
            this.toDoubleEx() ?: default
        } catch (e: Throwable) {
            default
        }
    } else {
        this.toDoubleEx() ?: default
    }
}

/** 转为int, 如果异常或为null,返回 [default] */
fun String?.toIntOrElse(default: Int = 0, exception: Boolean = false): Int {
    return if (exception) {
        try {
            this?.toInt() ?: default
        } catch (e: Throwable) {
            e.printStackTrace()
            default
        }
    } else {
        this?.toInt() ?: default
    }
}

/**
 * 针对 0.00 0 这样的数据,当做空"" 字符串处理,
 * @param blank String 0 的话是否返回 ""
 */
fun String?.getZeroAsEmpty(blank: Boolean = true): String? {
    if (this == null) {
        return null
    }
    if (this.isNullOrBlank()) {
        return ""
    }
    if (!blank) {
        return this.toDoubleEx().formatStr()
    }
    if (this.toDouble() == 0.0) {
        return ""
    }
    return this.toDoubleEx().formatStr()
}
