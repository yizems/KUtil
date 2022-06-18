package cn.yizems.util.ktx.okhttp.android

import android.os.ParcelFileDescriptor
import cn.yizems.util.ktx.okhttp.MEDIA_TYPE_FORM_DATA
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.FileInputStream
import java.io.IOException


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
                source?.closeQuietly()
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
