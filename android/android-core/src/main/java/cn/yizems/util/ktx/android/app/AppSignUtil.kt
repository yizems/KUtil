package cn.yizems.util.ktx.android.app

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.os.Bundle
import cn.yizems.util.ktx.android.context.getGlobalContext
import okio.ByteString.Companion.toByteString

/**
 * App 签名工具类
 */
object AppSignUtil {
    /**
     * 获取应用签名
     */
    @Suppress("DEPRECATION")
    fun getSign(packageName: String = getGlobalContext().packageName): Array<out Signature>? {
        val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getGlobalContext().packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
        } else {
            getGlobalContext().packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
        }
        return appInfo.signatures
    }


    /**
     * 获取应用签名Md5
     */
    fun getSignMD5(packageName: String = getGlobalContext().packageName): String? {
        val sign = getSign(packageName) ?: return null
        return sign.firstOrNull()?.toByteArray()?.let {
            it.toByteString(0, it.size)
        }?.md5()?.utf8()
    }

    /**
     * 获取应用签名Sha1
     */
    fun getSignSHA1(packageName: String = getGlobalContext().packageName): String? {
        val sign = getSign(packageName) ?: return null
        return sign.firstOrNull()?.toByteArray()?.let {
            it.toByteString(0, it.size)
        }?.sha1()?.utf8()
    }

    /**
     * 获取应用签名Sha256
     */
    fun getSignSHA256(packageName: String = getGlobalContext().packageName): String? {
        val sign = getSign(packageName) ?: return null
        return sign.firstOrNull()?.toByteArray()?.let {
            it.toByteString(0, it.size)
        }?.sha256()?.utf8()
    }

    /**
     * 获取某一个应用的MetaData
     */
    @SuppressLint("WrongConstant")
    @Suppress("DEPRECATION")
    fun getMetaData(packageName: String = getGlobalContext().packageName): Bundle? {
        val appInfo =
            getGlobalContext().packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        return appInfo.metaData
    }
}
