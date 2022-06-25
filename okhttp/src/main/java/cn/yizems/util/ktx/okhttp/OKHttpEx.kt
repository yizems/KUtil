package cn.yizems.util.ktx.okhttp

import cn.yizems.util.ktx.comm.file.generateUuidName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

//region media type

/** Json请求header, 包含charset */
const val MEDIA_TYPE_JSON_STR_WITH_CHARSET = "application/json;charset=utf-8"

/** Json请求Header*/
const val MEDIA_TYPE_JSON_STR = "application/json"

/** Json请求 MediaType */
val MEDIA_TYPE_JSON by lazy { MEDIA_TYPE_JSON_STR_WITH_CHARSET.toMediaType() }

/** form 请求 mediaType */
val MEDIA_TYPE_FORM_DATA by lazy { "multipart/form-data".toMediaType() }

//endregion


//region Body创建

/** String 转为 json 请求体 */
fun String?.toJsonRequestBody() = (this ?: "{}").toRequestBody(MEDIA_TYPE_JSON)

/** 文件请求体 */
fun File.toRequestBody() = this.asRequestBody(MEDIA_TYPE_FORM_DATA)

/** 文件转为 MultipartBody.Part */
fun File.toMultiPart(key: String = "file", fileName: String? = null): MultipartBody.Part {
    val rFileName = fileName ?: this.generateUuidName()
    return MultipartBody.Part.createFormData(key, rFileName, this.toRequestBody())
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
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

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

