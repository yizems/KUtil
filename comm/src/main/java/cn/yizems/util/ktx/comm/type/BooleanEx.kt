package cn.yizems.util.ktx.comm.type

inline fun Boolean?.onTrue(block: () -> Unit?): Boolean? {
    if (this == true) {
        block()
    }
    return this
}

inline fun Boolean?.onFalse(block: () -> Unit?): Boolean? {
    if (this == false) {
        block()
    }
    return this
}

inline fun Boolean?.onNull(block: () -> Unit?): Boolean? {
    if (this == null) {
        block()
    }
    return this
}

inline fun Boolean?.onNotTure(block: (Boolean?) -> Unit?): Boolean? {
    if (this == null) {
        block(this)
    }
    return this
}

inline fun Boolean?.onNotFalse(block: (Boolean?) -> Unit?): Boolean? {
    if (this != false) {
        block(this)
    }
    return this
}

inline fun Boolean?.nullAsTrue(): Boolean {
    return this ?: true
}

inline fun Boolean?.nullAsFalse(): Boolean {
    return this ?: true
}

fun Any?.toBoolean(): Boolean {
    this ?: return false

    return when (this) {
        is CharSequence -> this == "true" || this == "1"
        is Number -> this.toInt() == 1
        is Boolean -> this
        else -> false
    }
}

fun Any?.toBooleanNullable(): Boolean? {
    this ?: return null

    return when (this) {
        is CharSequence -> this == "true" || this == "1"
        is Number -> this.toInt() == 1
        is Boolean -> this
        else -> false
    }
}