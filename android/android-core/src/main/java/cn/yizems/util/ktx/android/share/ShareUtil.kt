package cn.yizems.util.ktx.android.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import cn.yizems.util.ktx.android.uri.getUri
import java.io.File


/**
 * 调用系统分享进行数据分享
 */
object ShareUtil {

    /** 默认分享的title,如果不设置,在手机上的效果和默认的效果不一样 */
    private const val DEFAULT_SHARE_TITLE = "分享"

    /**
     * 分享文本
     */
    @JvmStatic
    fun shareText(context: Context, text: String, title: String = DEFAULT_SHARE_TITLE) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, title))
    }

    /** 分享一张图片 */
    @JvmStatic
    fun shareImage(context: Context, file: File, title: String = DEFAULT_SHARE_TITLE) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, file.getUri(context))
        context.startActivity(Intent.createChooser(intent, title))
    }

    /**
     * 分享多张图片
     */
    @JvmStatic
    fun shareMulImage(
        context: Context,
        files: ArrayList<File>,
        title: String = DEFAULT_SHARE_TITLE
    ) {
        val uris = ArrayList<Uri>()

        files.forEach {
            uris.add(it.getUri(context))
        }
        val mulIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        mulIntent.type = "image/*"
        context.startActivity(Intent.createChooser(mulIntent, title))
    }

    /** 分享一个文件 */
    @JvmStatic
    fun shareFile(
        context: Context, file: File,
        mime: String = "*/*",
        title: String = DEFAULT_SHARE_TITLE
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = mime
        intent.putExtra(Intent.EXTRA_STREAM, file.getUri(context))
        context.startActivity(Intent.createChooser(intent, title))
    }

    /** 分享多个文件 */
    @JvmStatic
    fun shareMulFile(
        context: Context, files: ArrayList<File>,
        mime: String = "*/*",
        title: String = DEFAULT_SHARE_TITLE
    ) {
        val uris = ArrayList<Uri>()
        files.forEach {
            uris.add(it.getUri(context))
        }
        val mulIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        mulIntent.type = "mime"
        context.startActivity(Intent.createChooser(mulIntent, title))
    }
}
