package cn.yizems.util.ktx.android.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import cn.yizems.util.ktx.android.context.getGlobalContext
import java.io.File

/**
 * APK 工具类
 */
class ApkUtil {
    /**
     * 安装APK
     * @param downloadApk String
     * @param context Context?
     */
    fun installApk(downloadApk: String, context: Context? = null) {
        val shadowContext = context ?: getGlobalContext()
        val intent = Intent(Intent.ACTION_VIEW)
        val file = File(downloadApk)
//        Log.d("安装", "安装路径==$downloadApk")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri = FileProvider.getUriForFile(
                shadowContext, shadowContext.applicationContext.packageName + ".fileprovider",
                file
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val uri = Uri.fromFile(file)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        }
        shadowContext.startActivity(intent)
    }
}
