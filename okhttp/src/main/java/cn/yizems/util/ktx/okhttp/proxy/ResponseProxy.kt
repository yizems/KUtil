package cn.yizems.util.ktx.okhttp.proxy

import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 接口响应体代理类, 用于构建流代理的形式获取响应体
 * warn: 如果你的响应体很大的话, 不要直接读取到内存, 最好是写入到文件中, 再从文件中分批获取数据
 *      这种情况下,建议参照着写一个新的类实现
 */
object ResponseProxy {

    /**
     * 通过代理响应体获取响应体
     * 建议提前判断响应体类型,响应体大小,响应体编码等信息,再决定是否使用代理
     */
    fun proxy(
        response: Response,
        listener: ResponseProxyListener
    ): Response {

        if (!response.isSuccessful) {
            listener.onOmit("not successful")
            return response
        }

        val responseStream = response.body?.byteStream()

        if (responseStream == null) {
            listener.onOmit("body stream is null, omit")
            return response
        }

        val proxyStream =
            InputStreamProxy(responseStream, object : InputStreamProxy.InputStreamListener {
                override fun onEOF(outputStream: ByteArrayOutputStream) {
                    listener.onEOF(outputStream)
                }

                override fun onError(ex: IOException) {
                    listener.onError(ex)
                }
            })

        return response.newBuilder()
            .body(ForwardingResponseBody(response.body!!, proxyStream))
            .build()
    }

    interface ResponseProxyListener {
        /** 请求结束 */
        fun onEOF(outputStream: ByteArrayOutputStream)

        /** 出现异常 */
        fun onError(ex: IOException)

        /** 跳过,msg 是原因,目前只有不成功的时候才跳过 */
        fun onOmit(msg: String)
    }
}


class ForwardingResponseBody(
    private val mBody: ResponseBody,
    private val interceptedStream: InputStream
) : ResponseBody() {

    private val mInterceptedSource = interceptedStream.source().buffer()

    override fun contentType(): MediaType? {
        return mBody.contentType()
    }

    override fun contentLength(): Long {
        return mBody.contentLength()
    }

    override fun source(): BufferedSource {
        return mInterceptedSource
    }
}

