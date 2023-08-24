package cn.yizems.util.ktx.comm.type

/**
 * Null 时执行 block
 */
inline fun <T> T?.runOnNull(block: () -> Unit) = this.apply {
    if (this == null) {
        block()
    }
}

/**
 * Null 时 使用 block 返回的值
 */
inline fun <T> T?.nullAs(block: () -> T) = this ?: block()

fun <T> T?.nullAs(other: T) = this ?: other

