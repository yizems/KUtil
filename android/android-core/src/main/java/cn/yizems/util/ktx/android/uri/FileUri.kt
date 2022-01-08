@file:Suppress("NOTHING_TO_INLINE")

package cn.yizems.util.ktx.android.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File

/**
 * 获取File的Uri,兼容Android 7 的fileProvider
 */
inline fun File.getUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, context.packageName + ".fileprovider", this)
    } else {
        Uri.fromFile(this)
    }
}

data class FileInfo(
    var length: Long,
    var name: String,
    var uri: Uri
)

/**
 * 获取文件信息,主要是外部的文件uri
 * @receiver Context
 * @param uriString String
 * @return FileInfo
 */
fun Context.getFileInfo(uriString: String): FileInfo {
    return getFileInfo(uriString.toUri())
}

/**
 * 获取文件信息,主要是外部的文件uri
 *
 * @receiver Context
 * @param uri Uri
 * @return FileInfo
 */
fun Context.getFileInfo(uri: Uri): FileInfo {
    val query = contentResolver.query(
        uri, arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.SIZE
        ), null, null, null
    )!!

    query.moveToFirst()

    val name = query.getString(query.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
    val length = query.getLong(query.getColumnIndex(MediaStore.MediaColumns.SIZE))
    query.close()

    return FileInfo(length, name, uri)
}
