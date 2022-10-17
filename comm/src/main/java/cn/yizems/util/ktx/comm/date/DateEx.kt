package cn.yizems.util.ktx.comm.date

import java.text.SimpleDateFormat
import java.util.*

/**
 * [Date]格式化为字符串
 */
fun Date.format(format: String): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(this)
}

/**
 * 将时间戳转换为 [Date]
 */
fun Long.toDate(): Date {
    return Date(this)
}

/**
 * 字符串转为 [Date]
 * @param format 格式化字符串，如：yyyy-MM-dd HH:mm:ss
 * @param throwException 如果转换失败，是否抛出异常, 如果为false，则返回null
 */
fun String.toDate(format: String, throwException: Boolean = false): Date? {
    val sdf = SimpleDateFormat(format)
    return try {
        sdf.parse(this)
    } catch (e: Exception) {
        if (throwException) {
            throw e
        }
        null
    }
}

/**
 * 智能的将字符串转为 [Date],内置多种格式
 *
 * @param otherFormats 额外提供的格式化字符串
 */
fun String.toDateSmart(otherFormats: List<String>? = null): Date? {

    val formats = mutableListOf<String>(
        "yyyy-MM-dd HH:mm:ss.SSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy/MM/dd HH:mm:ss.SSS",
        "yyyy/MM/dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy/MM/dd'T'HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy/MM/dd HH:mm",
        "yyyy/MM/dd'T'HH:mm",
        "yyyy-MM-dd HH",
        "yyyy-MM-dd'T'HH",
        "yyyy/MM/dd HH",
        "yyyy/MM/dd'T'HH",
        "yyyy-MM-dd",
        "yyyy/MM/dd",
        "yyyy-MM",
        "yyyy/MM",
        "yyyy",
    )

    formats.forEach {
        val date = this.toDate(it, false)
        if (date != null) {
            return date
        }
    }

    return null
}
