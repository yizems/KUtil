package cn.yizems.util.ktx.android.context

import android.content.Context
import android.content.pm.PackageInfo

@Suppress("DEPRECATION")
val Context.appVersionNo: Int
    get() =
        getPackageInfo()
            .versionCode

val Context.appVersionName: String
    get() =
        getPackageInfo()
            .versionName

/**
 * @receiver Context
 * @param flag Int [android.content.pm.PackageManager.PackageInfoFlags] default 0
 * @param packageName default self packageName
 */
fun Context.getPackageInfo(flag: Int = 0, packageName: String = this.packageName): PackageInfo {
    return packageManager.getPackageInfo(packageName, flag)
}
