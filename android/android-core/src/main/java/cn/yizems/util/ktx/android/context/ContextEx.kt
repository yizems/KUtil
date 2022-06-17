package cn.yizems.util.ktx.android.context

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.util.TypedValue
import cn.yizems.util.ktx.android.dimens.px2dp

/** 获取当前应用的版本号 */
@Suppress("DEPRECATION")
val Context.appVersionNo: Long
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getPackageInfo()
                .longVersionCode
        } else {
            getPackageInfo()
                .versionCode.toLong()
        }

/** 获取当前应用的版本名 */
val Context.appVersionName: String
    get() =
        getPackageInfo()
            .versionName

/** 获取当前应用的 [PackageInfo] */
fun Context.getPackageInfo(flag: Int = 0, packageName: String = this.packageName): PackageInfo {
    return packageManager.getPackageInfo(packageName, flag)
}


fun Context.dp2px(dpVal: Float): Int {
    1.px2dp()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpVal, this.resources.displayMetrics
    ).toInt()
}

fun Context.sp2px(spVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spVal, resources.displayMetrics
    ).toInt()
}

fun Context.px2dp(pxVal: Float): Float {
    val scale = resources.displayMetrics.density
    return pxVal / scale
}

fun Context.px2sp(pxVal: Float): Float {
    return pxVal / resources.displayMetrics.scaledDensity
}
