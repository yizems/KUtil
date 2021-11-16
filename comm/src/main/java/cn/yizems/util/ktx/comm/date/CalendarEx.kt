package com.sdgh.oa.powerchina.utils

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.setFieldEx(
    year: Int? = null,
    month: Int? = null,
    day: Int? = null,
    hourOfDay: Int? = null,
    minute: Int? = null,
    second: Int? = null,
    millions: Int? = null,
): Calendar {
    if (year != null) {
        this.set(Calendar.YEAR, year)
    }
    if (month != null) {
        this.set(Calendar.MONTH, month)
    }
    if (day != null) {
        this.set(Calendar.DAY_OF_MONTH, day)
    }
    if (hourOfDay != null) {
        this.set(Calendar.HOUR_OF_DAY, hourOfDay)
    }
    if (minute != null) {
        this.set(Calendar.MINUTE, minute)
    }
    if (second != null) {
        this.set(Calendar.SECOND, second)
    }
    if (millions != null) {
        this.set(Calendar.MILLISECOND, millions)
    }

    return this
}


/**
 * 设置 [cField] 字段之后的值为开始或结束
 *
 * @param cField Int [Calendar.MONTH]
 */
fun Calendar.toFieldStartOrEnd(
    cField: CalendarField = CalendarField.YEAR,
    start: Boolean,
): Calendar {

    val optionFields = CalendarField.values().filter { it.field > cField.field }

    optionFields.reversed()
        .forEach {
            this.set(it.field, this.getActualMinimum(cField.field))
        }

    optionFields
        .forEach {
            val value =
                if (start) this.getActualMinimum(it.field) else this.getActualMaximum(it.field)
            this.set(it.field, value)
        }

    return this
}

enum class CalendarField(val field: Int) {
    YEAR(Calendar.YEAR),
    MONTH(Calendar.MONTH),
    DAY_OF_MONTH(Calendar.DAY_OF_MONTH),
    HOUR_OF_DAY(Calendar.HOUR_OF_DAY),
    MINUTE(Calendar.MINUTE),
    SECOND(Calendar.SECOND),
    MILLISECOND(Calendar.MILLISECOND),
    ;
}


fun Calendar.format(format: String = "yyyy-MM-dd HH:mm:ss.SSS"): String {
    return SimpleDateFormat(format)
        .format(this.time)
}
