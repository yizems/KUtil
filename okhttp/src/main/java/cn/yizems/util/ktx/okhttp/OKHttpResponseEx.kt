package cn.yizems.util.ktx.okhttp

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.internal.headersContentLength
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

/** 是否为 gizp 压缩 */
fun Response.isGzip(): Boolean {
    return this.transferEncoding() == "gzip"
}

fun Response.isJson(): Boolean {
    return mediaType()?.subtype == "json"
}


/**
 * 获取文本内容, 已对gzip进行解压缩
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

/**
 * clone body buffer, 用于额外读取
 */
fun Response.cloneBodyBuffer(): Buffer? {
    val source: BufferedSource = this.body?.source() ?: return null
    // Buffer the entire body.
    source.request(Long.MAX_VALUE)
    return source.buffer.clone()
}

/**
 * 按照顺序依次往下取, 直到取到为止,取不到为-1
 * 1. header
 * 2. body.length
 * 3. body.source.buffer.size: 不一定准确: 特别是在 stream的时候
 * @receiver Response
 * @return Long
 */
fun Response.contentLengthCompat(): Long {
    var length = headersContentLength()
    if (length > 0) {
        return length
    }
    length = this.body?.contentLength() ?: -1L
    if (length > 0) {
        return length
    }
    length = this.body?.source()?.buffer?.size ?: -1
    return length
}
