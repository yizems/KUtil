package cn.yizems.util.ktx.comm.file

import cn.yizems.util.ktx.comm.random.uuid
import java.io.File

fun File.uuidName(): String {
    return this.name.let {
        if (it.contains(".")) {
            uuid() + "." + it.substringAfterLast(".")
        } else {
            uuid()
        }
    }
}