package cn.yizems.util.ktx.comm.type

/**
 * Null 时执行 block
 */
fun Any?.runOnNull(block: () -> Unit) {
    if (this == null) {
        block()
    }
}

