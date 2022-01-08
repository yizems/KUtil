package cn.yizems.util.ktx.comm.type

fun Any?.runOnNull(block: () -> Unit) {
    if (this == null) {
        block()
    }
}

