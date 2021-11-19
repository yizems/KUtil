package cn.yizems.util.ktx.okhttp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.ParcelFileDescriptor
import cn.yizems.util.ktx.comm.file.uuidName
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

//region media type

const val MEDIA_TYPE_JSON_STR = "application/json; charset=utf-8"

val MEDIA_TYPE_JSON by lazy { MEDIA_TYPE_JSON_STR.toMediaType() }

const val HEADER_MEDIA_JSON_STR = "Content-Type:$MEDIA_TYPE_JSON_STR"

val MEDIA_TYPE_FORM_DATA by lazy { "multipart/form-data".toMediaType() }

//endregion


//region Body创建


fun String?.toJsonRequestBody() = (this ?: "{}").toRequestBody(MEDIA_TYPE_JSON)

fun File.toRequestBody() = this.asRequestBody(MEDIA_TYPE_FORM_DATA)

fun File.toMultiPart(key: String = "file", fileName: String? = null): MultipartBody.Part {
    val rFileName = fileName ?: this.uuidName()
    return MultipartBody.Part.createFormData(key, rFileName, this.toRequestBody())
}

/**
 * 将 ParcelFileDescriptor 文件转换为 RequestBody
 * 适用于外部选择文件
 * @receiver ParcelFileDescriptor
 * @return RequestBody
 * @throws IOException
 */
fun ParcelFileDescriptor.toRequestBody(): RequestBody {
    val stream = FileInputStream(this.fileDescriptor)
    val fd = this
    return object : RequestBody() {
        override fun contentType(): MediaType {
            return MEDIA_TYPE_FORM_DATA
        }

        override fun contentLength(): Long {
            return try {
                stream.available().toLong()
            } catch (e: IOException) {
                0
            }
        }

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            var source: Source? = null
            try {
                source = FileInputStream(fd.fileDescriptor).source()
                sink.writeAll(source)
            } finally {
                source!!.closeQuietly()
            }
        }
    }
}

/**
 * 将 ParcelFileDescriptor 转换为 MultipartBody.Part
 * 适用于外部选择文件
 * @receiver ParcelFileDescriptor
 * @param key String
 * @return MultipartBody.Part
 */
fun ParcelFileDescriptor.toMultiPart(key: String = "file"): MultipartBody.Part {
    return MultipartBody.Part.createFormData(key, "", this.toRequestBody())
}

//endregion


//region OkhttpClient 配置

/**
 * 信任所有域名
 * @receiver OkHttpClient.Builder
 * @return OkHttpClient.Builder
 */
fun OkHttpClient.Builder.trustAllCerts(): OkHttpClient.Builder {

    val trustManager = object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    val trustManagers = arrayOf(trustManager)

    val sslContext = SSLContext.getInstance("TSL");
    sslContext.init(null, trustManagers, SecureRandom());
    val sslSocketFactory = sslContext.socketFactory
    this.sslSocketFactory(sslSocketFactory, trustManager)
    this.hostnameVerifier { _, _ -> true };
    return this

}

//endregion

