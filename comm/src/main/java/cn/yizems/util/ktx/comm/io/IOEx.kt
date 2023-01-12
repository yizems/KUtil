package cn.yizems.util.ktx.comm.io

import okio.Buffer
import okio.EOFException
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

/**
 * 判断流第一个字节是否utf8
 *
 *  65533 = Utf8.kt: REPLACEMENT_CODE_POINT
 */
fun Buffer.startUtf8(): Boolean {
    return try {
        readUtf8CodePoint() != 65533
    } catch (e: EOFException) {
        false
    }
}

/**
 * 第一个字节是否utf8
 *
 *  65533 = Utf8.kt: REPLACEMENT_CODE_POINT
 */
fun ByteArray.startUtf8(): Boolean {
    return Buffer().write(this).startUtf8()
}

/**
 * 利用前 64 个字节判断是否为utf8编码
 * @return true utf8
 */
fun Buffer.isUtf8(): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = minOf(size, 64)
        copyTo(prefix, 0, byteCount)

        for (i in 0 until 16) {
            if (prefix.exhausted()) {
                return false
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (e: Exception) {
        return false
    }
}

/**
 * 利用前 64 个字节判断是否为utf8编码
 * @return true utf8
 */
fun ByteArray.isUtf8(): Boolean {
    return Buffer().write(this).isUtf8()
}
