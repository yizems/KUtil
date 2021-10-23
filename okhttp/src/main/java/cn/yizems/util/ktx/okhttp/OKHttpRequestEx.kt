package cn.yizems.util.ktx.okhttp

import okhttp3.Request
import okio.Buffer


fun Request.contentType(): String? {
    return this.header("Content-Type") ?: body?.contentType()?.toString()
}

fun Request.acceptEncoding(): String? {
    return this.header("Accept-Encoding")
}

fun Request.getQuery(): Map<String, String?> {
    return url.queryParameterNames.associateWith { url.queryParameter(it) }
}


/**
 * 获取文本内容
 * @receiver Response
 * @return Boolean
 */
fun Request.readBodyString(): String? {
    val body = body ?: return null
    val buffer = Buffer()
    body.writeTo(buffer)
    return buffer.readString(Charsets.UTF_8)
}




