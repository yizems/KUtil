package cn.yizems.util.ktx.comm.date

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

fun Calendar.toDayStart(): Calendar {
    setFieldEx(
        hourOfDay = 0,
        minute = 0,
        second = 0,
        millions = 0
    )
    return this
}

fun Calendar.toDayEnd(): Calendar {
    setFieldEx(
        hourOfDay = 23,
        minute = 59,
        second = 59,
        millions = 999
    )
    return this
}

fun Calendar.millionsToStart(): Calendar {
    setFieldEx(
        millions = 0
    )
    return this
}

fun Calendar.millionsToEnd(): Calendar {
    setFieldEx(
        millions = 999
    )
    return this
}