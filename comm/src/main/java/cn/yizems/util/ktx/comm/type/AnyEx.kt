package cn.yizems.util.ktx.comm.type

/**
 * Null 时执行 block
 */
inline fun <T> T?.runOnNull(block: () -> Unit) = this.apply {
    if (this == null) {
        block()
    }
}
