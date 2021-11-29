package cn.yizems.util.ktx.comm.crypto.digest

import okio.ByteString.Companion.encodeUtf8
import okio.ByteString.Companion.toByteString
import java.io.File
import java.security.MessageDigest

/**
 * 数字摘要方法
 */

//region 字符串, 更多参照 [ByteString]

/**
 * 字符串md5
 * @receiver String
 * @return String
 */
fun String.md5() = this.encodeUtf8().md5().hex()

fun String.sha1() = this.encodeUtf8().sha1().hex()

fun String.sha256() = this.encodeUtf8().sha256()

fun String.sha512() = this.encodeUtf8().sha512()

fun String.hex() = this.encodeUtf8().hex()

fun String.base64() = this.encodeUtf8().base64()

fun String.base64Url() = this.encodeUtf8().base64Url()

//endregion

fun File.md5(): String {
    if (!this.exists()) {
        throw IllegalArgumentException("file not exists")
    }

    return digest("MD5")
}

fun File.sha1(): String {
    if (!this.exists()) {
        throw IllegalArgumentException("file not exists")
    }

    return digest("SHA")
}

fun File.sha256(): String {
    if (!this.exists()) {
        throw IllegalArgumentException("file not exists")
    }

    return digest("SHA-256")
}

fun File.sha512(): String {
    if (!this.exists()) {
        throw IllegalArgumentException("file not exists")
    }

    return digest("SHA-512")
}

private fun File.digest(type: String): String {
    if (!this.exists()) {
        throw IllegalArgumentException("file not exists")
    }

    val messageDigest = MessageDigest.getInstance(type)

    this.forEachBlock { buffer, bytesRead ->
        messageDigest.update(buffer, 0, bytesRead)
    }
    return messageDigest.digest().toByteString().hex()
}


fun File.base64(): String {
    if (!this.exists()) {
        throw IllegalArgumentException("file not exists")
    }

    return this.readBytes().toByteString()
        .base64()
}

