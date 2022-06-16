package cn.yizems.util.ktx.comm.io

import java.io.File
import java.io.InputStream

/**
 * 写入文件,并关闭流
 * 注意, 如果文件存在,会自动删除
 *
 * @param file 文件
 */
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
