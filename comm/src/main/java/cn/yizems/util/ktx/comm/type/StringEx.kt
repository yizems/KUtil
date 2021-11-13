@file:Suppress("UNCHECKED_CAST")

package cn.yizems.util.ktx.comm.type

import cn.yizems.util.ktx.comm.number.formatMoney
import java.util.*


infix fun <T : CharSequence?> CharSequence?.nullOr(other: T): T {
    if (this == null) {
        return other
    }
    return this.toString() as T
}

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

infix fun <T : CharSequence?> CharSequence?.emptyOr(other: T): T {
    if (this.isNullOrEmpty()) {
        return other
    }
    return this.toString() as T
}

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

infix fun <T : CharSequence?> CharSequence?.blankOr(other: T): T {
    if (this.isNullOrBlank()) {
        return other
    }
    return this.toString() as T
}

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

/**
 * 删除秒和.000
 * :2018-02-02 12:00:00.000
 * @receiver String
 * @param default String
 * @return String
 */
fun String?.dropSecond(): String {
    val temp = dropDatePoint()

    if (temp.contains(":")) {
        return temp.substring(0, temp.lastIndexOf(":"))
    }
    return temp
}


/**
 * 时间格式 2012-01-02 12:00:00 获取到 2012-01-02
 * @receiver String?
 * @return String
 */
fun String?.getTimeYMD(): String {
    if (this.isNullOrBlank()) {
        return ""
    }
    if (this.contains(" ")) {
        return this.split(" ")[0]
    }
    return this
}

/**
 * 处理0001-01-01 00:00:00 这种时间戳为0的时间
 * @receiver String?
 * @return Boolean
 */
fun String?.isTimeEmpty(): Boolean {
    if (this.isNullOrBlank()) {
        return true
    }
    return this.startsWith("0001")
}

/**
 * 获取时间
 * 0001-01-01 00:00:00 这种时间戳为0的时间 返回""
 * @receiver String?
 * @return String
 */
fun String?.getTime(): String {
    if (this.isTimeEmpty()) {
        return ""
    }
    return this!!
}

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
 * 默认为0.0
 */
fun String?.toDoubleOrElse(default: Double = 0.0, ignoreException: Boolean = true): Double {
    if (ignoreException) {
        try {
            return this.toDoubleEx() ?: default
        } catch (e: Throwable) {
            e.printStackTrace()
            return default
        }
    } else {
        return this.toDoubleEx() ?: default
    }
}

fun String?.toIntEx(): Int? {
    if (this.isNullOrBlank()) {
        return null
    }
    return this.toInt()
}

fun String?.toIntOrElse(default: Int = 0, exception: Boolean = false): Int {
    if (exception) {
        try {
            return this.toIntEx() ?: default
        } catch (e: Throwable) {
            e.printStackTrace()
            return default
        }
    } else {
        return this.toIntEx() ?: default
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
        return this.toDoubleEx().formatMoney()
    }
    if (this.toDouble() == 0.0) {
        return ""
    }
    return this.toDoubleEx().formatMoney()
}
