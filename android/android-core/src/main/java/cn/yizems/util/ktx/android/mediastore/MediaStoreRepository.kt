package cn.yizems.util.ktx.android.mediastore

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import cn.yizems.util.ktx.android.context.getGlobalContext
import cn.yizems.util.ktx.comm.random.uuid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream

/**
 * [android.provider.MediaStore] 辅助类, 方便存储文件和查询文件
 *
 * 后续完善该类
 */
@Suppress("BlockingMethodInNonBlockingContext")
object MediaStoreRepository {

    /**
     * 插入文件
     * @param dir String 插入到哪个目录: like [Environment.DIRECTORY_PICTURES]
     * @param file File
     * @param title String?
     */
    @Suppress("DEPRECATION")
    suspend fun insertFile(dir: String, file: File, title: String? = null) =
        withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                insertFileOnQ(dir, file, title)
            } else {
                val targetFile = File(
                    Environment.getExternalStoragePublicDirectory(dir)
                        .absolutePath + File.separator + (title ?: file.name)
                )
                file.copyTo(targetFile, true)
            }
        }


    /**
     * @param file File
     * @param title String?
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun insertFileOnQ(dir: String, file: File, title: String? = null) =
        withContext(Dispatchers.IO) {
            val uri = insertToMediaStoreRecord(dir, title ?: file.name)
            file.inputStream().use { inputStream ->
                getGlobalContext()
                    .contentResolver
                    .openOutputStream(uri, "w")!!
                    .use { outputStream: OutputStream ->
                        inputStream.copyTo(outputStream)
                    }
            }

        }

    /**
     * 插入图片到指定文件夹
     * @param title 不需要携带 后缀, 会自动根据 [format] 自动添加
     * @param format bitmap 转换格式, 只支持 JPEG,PNG,WEBP
     */
    @Suppress("DEPRECATION")
    suspend fun insertBitmap(
        dir: String,
        bitmap: Bitmap,
        title: String = uuid(),
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    ) = withContext(Dispatchers.IO) {
        val supportFormat = arrayOf(
            Bitmap.CompressFormat.JPEG,
            Bitmap.CompressFormat.PNG,
            Bitmap.CompressFormat.WEBP
        )

        val compatFormat = if (format !in supportFormat) {
            Bitmap.CompressFormat.PNG
        } else {
            format
        }

        val fileName = "$title." + when (compatFormat) {
            Bitmap.CompressFormat.JPEG -> "jpg"
            Bitmap.CompressFormat.PNG -> "png"
            Bitmap.CompressFormat.WEBP -> "webp"
            else -> {
                "png"
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val uri = insertToMediaStoreRecord(dir, fileName)
            getGlobalContext().contentResolver
                .openOutputStream(uri, "w")
                .use {
                    bitmap.compress(compatFormat, 100, it)
                }
        } else {
            val targetFile = File(
                Environment.getExternalStoragePublicDirectory(dir)
                    .absolutePath + File.separator + fileName
            )
            targetFile.outputStream().use {
                bitmap.compress(compatFormat, 100, it)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun insertToMediaStoreRecord(
        dir: String,
        fileName: String,
    ): Uri = withContext(Dispatchers.IO) {
        val newValues = ContentValues(2)
        newValues.put(
            MediaStore.Downloads.TITLE,
            fileName.substringAfter(".")
        )
        newValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        newValues.put(MediaStore.Downloads.RELATIVE_PATH, dir)

        val exContentUri = when (dir) {
            Environment.DIRECTORY_DCIM,
            Environment.DIRECTORY_SCREENSHOTS,
            Environment.DIRECTORY_PICTURES -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            Environment.DIRECTORY_DOWNLOADS -> MediaStore.Downloads.EXTERNAL_CONTENT_URI
            else -> throw IllegalArgumentException("不支持的 文件夹类型,当前为: $dir")
        }
        return@withContext getGlobalContext()
            .contentResolver
            .insert(exContentUri, newValues)!!
    }
}
