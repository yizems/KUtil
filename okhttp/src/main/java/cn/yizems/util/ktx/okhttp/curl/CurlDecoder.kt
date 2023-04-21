package util

import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.commons.lang3.StringEscapeUtils
import java.net.URLDecoder
import kotlin.math.min

class CurlDecoder(
    private val curlStr: String
) {
    private var optIndex = 0

    private val headers = mutableMapOf<String, String>()
    private var url: String? = null
    private var method: String = ""
    private var bodyStr: String? = null
    private val formData = mutableMapOf<String, String>()

    fun decode(): CurlDecoder {
        while (optIndex < curlStr.length) {
            val str = prePeek()
            when {
                str.startsWith(" ") -> {
                    ++optIndex
                }

                str.startsWith("curl") -> {
                    optIndex += 4
                }

                str.startsWith("-H") -> {
                    val header = decodeValue()
                    val headerKey = header.substringBefore(":").trim()
                    val value = header.substringAfter(":").trim()
                    if (ignoreHeader(headerKey)) {
                        continue
                    }
                    headers[headerKey] = value
                }

                str.startsWith("-F") -> {
                    decodeValue().let {
                        formData[it.substringBefore("=").trim()] = it.substringAfter("=").trim()
                    }
                }

                str.startsWith("'http") || str.startsWith("\"http") -> {
                    url = URLDecoder.decode(decodeValue())
                }

                str.startsWith("-X") -> {
                    val endIndex = str.indexOf(" ", optIndex + 3)
                    method = str.substring(optIndex + 3, endIndex).trim()
                    optIndex = endIndex
                }

                str.startsWith("--dat")
                        || str.startsWith("-d") -> {
                    val value = decodeValue()
                    bodyStr = if (curlStr[optIndex] == '\"') {
                        StringEscapeUtils.unescapeJava(value)
                    } else {
                        value
                    }
                }

                else -> {
                    ++optIndex
                }
            }
        }

        when {
            method.isBlank() && (bodyStr != null || formData.isNotEmpty()) -> {
                method = "POST"
            }

            else -> {
                method = "GET"
            }
        }
        return this
    }


    private fun decodeValue(): String {
        var preChar: Char? = null
        var startChar: Char? = null
        val sb = StringBuilder()
        var lastIndex = 0
        for (index in optIndex until curlStr.length) {
            val cur = curlStr[index]
            if (isStartOrEnd(cur, preChar, startChar)) {
                if (startChar == null) { //开始
                    startChar = cur
                    continue
                } else { //结束
                    lastIndex = index
                    break
                }
            }
            if (startChar != null) {
                sb.append(cur)
            }
            preChar = cur
        }
        optIndex = lastIndex + 1
        return sb.toString().trim()
    }

    private fun isStartOrEnd(cur: Char, pre: Char?, startChar: Char? = null): Boolean {

        when (startChar) {
            '"' -> {
                return cur == '"' && pre != '\\'
            }

            '\'' -> {
                return cur == '\'' && pre != '\\'
            }
        }

        return (cur == '"' || cur == '\'') && pre != '\\'
    }

    private fun prePeek(length: Int = 5): String {
        return curlStr.substring(optIndex, min(optIndex + length, curlStr.length))
    }


    public fun getURL(): String {
        return url!!
    }

    fun getHeaders(): Map<String, String> {
        return headers
    }

    fun getMethod(): String {
        return method
    }

    fun getBodyStr(): String? {
        return bodyStr
    }

    fun getFormData(): Map<String, String> {
        return formData
    }

    override fun toString(): String {
        return """
url: $url
method: $method
headers: 
${headers.map { "${it.key}:${it.value}" }.joinToString("\n")}
bodyStr: $bodyStr
formData: 
${formData.map { "${it.key}:${it.value}" }.joinToString("\n")}
        """.trimIndent()
    }

    fun toOkhttpRequest(): okhttp3.Request {
        val builder = okhttp3.Request.Builder()
        headers.forEach {
            builder.addHeader(it.key, it.value)
        }
        builder.url(url!!)

        if (formData.isNotEmpty()) {
            val formBody = okhttp3.FormBody.Builder()
            formData.forEach {
                formBody.add(it.key, it.value)
            }
            builder.method(method, formBody.build())
            return builder.build()
        }
        if (bodyStr != null) {
            builder.method(method, bodyStr?.toRequestBody())
            return builder.build()
        }
        builder.method(method, null)
        return builder.build()
    }

    private fun ignoreHeader(headerKey: String): Boolean {
        return ignoreHeader.any { it.equals(headerKey, true) }
    }

    companion object {
        private val ignoreHeader = listOf(
            "authority",
            "referer",
        )
    }
}
