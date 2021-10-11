package cn.yizems.util.ktx.comm.io

import java.io.File
import java.io.InputStream

fun InputStream.writeTo(file: File) {
    if (file.exists()) {
        file.delete()
    }
    file.parentFile?.mkdirs()
    file.createNewFile()
    use { ins ->
        file.outputStream().use { outs ->
            ins.copyTo(outs)
        }
    }
}