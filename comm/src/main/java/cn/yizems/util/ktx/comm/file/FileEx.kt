@file:Suppress("NOTHING_TO_INLINE")

package cn.yizems.util.ktx.comm.file

import cn.yizems.util.ktx.comm.random.uuid
import java.io.File
import java.io.InputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.security.MessageDigest


inline fun String.toFile() = File(this)

fun File.generateUuidName(): String {
    return this.name.let {
        if (it.contains(".")) {
            uuid() + "." + it.substringAfterLast(".")
        } else {
            uuid()
        }
    }
}

/**
 * 重命名
 * @receiver File
 * @param name String 新文件名
 * @param renameSuffix Boolean 如果为false 则 [name] 包含后缀名,否则不包含
 */
fun File.rename(name: String, renameSuffix: Boolean = false) {
    this.renameTo(
        File(
            this.parentFileCompat(),
            name + (if (renameSuffix) "" else this.name.substringAfterLast("."))
        )
    )
}

/**
 * 防止部分情况下获取父文件[File] 为空的情况
 * @receiver File
 * @return File
 */
inline fun File.parentFileCompat() =
    this.absolutePath.substringBeforeLast(File.separator).toFile()

/**
 * 兄弟文件
 * @receiver File
 * @param name String
 */
inline fun File.brother(name: String) = File(parentFileCompat(), name)

/**
 * 子文件
 * @receiver File
 * @param name String
 */
inline fun File.child(name: String) = File(this, name)


fun File.getDirSize(): Long {
    var size: Long = 0
    if (!this.isDirectory) {
        return this.length()
    }
    val fileList = this.listFiles() ?: return 0L
    for (i in fileList.indices) {
        size = if (fileList[i].isDirectory) {
            size + fileList[i].getDirSize()
        } else {
            size + fileList[i].length()
        }
    }
    return size
}

/**
 * 格式化单位
 *
 * @param size
 * @return
 */
fun File.getFormatSize(): String {

    val size = this.getDirSize()

    val kiloByte = size / 1024
    if (kiloByte < 1) {
        return "0B"
    }
    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        val result1 = BigDecimal(kiloByte)
        return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
    }
    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        val result2 = BigDecimal(megaByte)
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
    }
    val teraBytes = gigaByte / 1024
    if (teraBytes < 1) {
        val result3 = BigDecimal(gigaByte)
        return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
    }
    val result4 = BigDecimal(teraBytes)
    return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
}


fun File.md5(): String? {
    if (!this.exists()) {
        return null
    }
    if (!this.isFile) {
        return null
    }

    val digest = MessageDigest.getInstance("MD5")
    val buffer = ByteArray(1024)
    this.inputStream().use { ins ->
        var len = -1
        while (ins.read(buffer, 0, 1024).also { len = it } != -1) {
            digest.update(buffer, 0, len)
        }
    }
    val bigInt = BigInteger(1, digest.digest())
    var md5 = bigInt.toString(16)
    for (i in 0 until 32 - md5.length) {
        md5 = "0$md5"
    }
    return bigInt.toString(16)
}

fun File.deleteDir(
    deleteThisPath: Boolean = false
) {
    if (!this.exists()) {
        return
    }
    if (this.isFile) {
        this.delete()
        return
    }

    if (this.isDirectory) {
        val files = listFiles() ?: return
        for (i in files.indices) {
            files[i].deleteDir(true)
        }
    }
    this.delete()
}

fun InputStream.writeToFile(fPath: String) {
    this.use { ins ->
        fPath.toFile()
            .apply {
                if (exists()) {
                    this.delete()
                }
            }
            .outputStream().use { ous ->
                ins.copyTo(ous)
            }
    }
}
