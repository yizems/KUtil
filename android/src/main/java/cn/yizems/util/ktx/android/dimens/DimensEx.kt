package cn.yizems.util.ktx.android.dimens

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.WindowManager

/**
 * 常用单位转换的辅助类
 */

/**
 * dp转px
 * @param context
 * @param dpVal
 * @return
 */
fun Context.dp2px(dpVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpVal, this.resources.displayMetrics
    ).toInt()
}

/**
 * sp转px
 *
 * @param context
 * @param spVal
 * @return
 */
fun Context.sp2px(spVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spVal, resources.displayMetrics
    ).toInt()
}

/**
 * px转dp
 *
 * @param context
 * @param pxVal
 * @return
 */
fun Context.px2dp(pxVal: Float): Float {
    val scale = resources.displayMetrics.density
    return pxVal / scale
}

/**
 * px转sp
 *
 * @param pxVal
 * @return
 */
fun Context.px2sp(pxVal: Float): Float {
    return pxVal / resources.displayMetrics.scaledDensity
}

@Suppress("DEPRECATION")
fun Context.screenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        wm.currentWindowMetrics.bounds.width()
    } else {
        wm.defaultDisplay.width
    }
}

@Suppress("DEPRECATION")
fun Context.screenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        wm.currentWindowMetrics.bounds.height()
    } else {
        wm.defaultDisplay.height
    }
}