package cn.yizems.util.ktx.okhttp.proxy

import java.io.ByteArrayOutputStream
import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream

/**
 * 流代理类, 参照facebook 相关的代码
 * 目前是使用的 ByteArrayOutputStream,
 * 如果你的响应体很大的话, 不要直接读取到内存, 最好是写入到文件中, 再从文件中分批获取数据
 */
class InputStreamProxy(
    inputStream: InputStream,
    private val listener: InputStreamListener
) : FilterInputStream(inputStream) {

    private val mOutputStream = ByteArrayOutputStream()
    private var mClosed: Boolean = false


    private var mSkipBuffer: ByteArray? = null

    private val skipBufferLocked: ByteArray
        get() {
            if (mSkipBuffer == null) {
                mSkipBuffer = ByteArray(BUFFER_SIZE)
            }
            return mSkipBuffer!!
        }

    @Synchronized
    private fun checkEOF(n: Int): Int {
        if (n == -1) {
            listener.onEOF(mOutputStream)
            closeOutputStreamQuietly()
        }
        return n
    }


    @Throws(IOException::class)
    override fun read(): Int {
        return try {
            val result = checkEOF(`in`.read())
            if (result != -1) {
                writeToOutputStream(result)
            }
            result
        } catch (ex: IOException) {
            throw handleIOException(ex)
        }
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray): Int {
        return this.read(b, 0, b.size)
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        return try {
            val result = checkEOF(`in`.read(b, off, len))
            if (result != -1) {
                writeToOutputStream(b, off, result)
            }
            result
        } catch (ex: IOException) {
            throw handleIOException(ex)
        }
    }

    @Synchronized
    @Throws(IOException::class)
    override fun skip(n: Long): Long {
        val buffer = skipBufferLocked
        var total: Long = 0
        while (total < n) {
            val bytesDiff = n - total
            val bytesToRead = Math.min(buffer.size.toLong(), bytesDiff).toInt()
            val result = this.read(buffer, 0, bytesToRead)
            if (result == -1) {
                break
            }
            total += result.toLong()
        }
        return total
    }


    override fun markSupported(): Boolean {
        // this can be implemented, but isn't needed for TeedInputStream's behavior
        return false
    }

    override fun mark(readlimit: Int) {
        // noop -- mark is not supported
    }

    @Throws(IOException::class)
    override fun reset() {
        throw UnsupportedOperationException("Mark not supported")
    }

    @Throws(IOException::class)
    override fun close() {
        super.close()
        closeOutputStreamQuietly()
    }

    @Synchronized
    private fun closeOutputStreamQuietly() {
        if (!mClosed) {
            try {
                mOutputStream.close()
            } catch (e: IOException) {
            } finally {
                mClosed = true
            }
        }
    }

    private fun handleIOException(ex: IOException): IOException {
        listener.onError(ex)
        return ex
    }

    @Synchronized
    private fun writeToOutputStream(oneByte: Int) {
        if (mClosed) {
            return
        }
        mOutputStream.write(oneByte)
    }

    @Synchronized
    private fun writeToOutputStream(b: ByteArray, offset: Int, count: Int) {
        if (mClosed) {
            return
        }
        mOutputStream.write(b, offset, count)
    }

    companion object {
        private const val BUFFER_SIZE = 1024
    }


    interface InputStreamListener {
        fun onEOF(outputStream: ByteArrayOutputStream)
        fun onError(ex: IOException)
    }
}
