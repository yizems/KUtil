package cn.yizems.util.ktx.okhttp

import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
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

fun RequestBody.readBodyToString(): String {
    val buffer = Buffer()
    this.writeTo(buffer)
    return buffer.readString(Charsets.UTF_8)
}

/**
 * get parts info
 * @receiver MultipartBody
 * @return Map<String?, String?>
 *     key:value
 *     key:fileName
 */
fun MultipartBody.toInfoMap(): Map<String?, String?> {
    return this.parts
        .associate {
            val contentDisposition = it.headers?.toMap()?.get("Content-Disposition")
                ?: return@associate null to null
            val itemContentType =
                it.body.contentType()?.toString() ?: return@associate null to null

            if (itemContentType.startsWith("multipart/form-data")) {
                //文件类型
                val key = contentDisposition.substringAfter("form-data; name=\"")
                    .substringBefore("\"")
                val fileName = contentDisposition.substringAfter("filename=\"")
                    .substringBefore("\"")
                return@associate key to fileName
            }
            // 文本类型
            if (itemContentType.startsWith("text/plain")) {
                val key = contentDisposition.substringAfter("name=\"")
                    .substringBefore("\"")
                val value = it.body.readBodyToString()
                return@associate key to value
            }
            return@associate null to null
        }
}

