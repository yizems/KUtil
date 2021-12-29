@file:Suppress("NOTHING_TO_INLINE")

package cn.yizems.util.ktx.comm.file

import cn.yizems.util.ktx.comm.random.uuid
import java.io.File


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
