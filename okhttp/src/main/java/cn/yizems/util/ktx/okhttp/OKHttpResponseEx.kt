package cn.yizems.util.ktx.okhttp

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okio.Buffer
import okio.BufferedSource
import okio.GzipSource
import okio.buffer


fun Response.contentType(): String? {
    return this.header("Content-Type")
}

fun Response.mediaType(): MediaType? {
    return this.body?.contentType() ?: contentType()?.toMediaTypeOrNull()
}

fun Response.transferEncoding(): String? {
    return this.header("Transfer-Encoding")
}

fun Response.isGzip(): Boolean {
    return this.transferEncoding() == "gzip"
}

/**
 * 获取文本内容
 * @receiver Response
 * @return Boolean
 */
fun Response.readBodyString(): String? {
    return if (isGzip()) {
        readBodyStringGzip()
    } else {
        readBodyStringNormal()
    }
}


fun Response.readBodyStringGzip(): String? {
    val cloneBodyBuffer = cloneBodyBuffer() ?: return null
    val gzipSource = GzipSource(cloneBodyBuffer)
    return gzipSource.buffer().use {
        it.readUtf8()
    }
}

fun Response.readBodyStringNormal(): String? {
    return cloneBodyBuffer()?.readUtf8()
}


fun Response.cloneBodyBuffer(): Buffer? {
    val source: BufferedSource = this.body?.source() ?: return null
    // Buffer the entire body.
    source.request(Long.MAX_VALUE)
    return source.buffer.clone()
}

fun Response.isJson(): Boolean {
    return mediaType()?.subtype == "json"
}
