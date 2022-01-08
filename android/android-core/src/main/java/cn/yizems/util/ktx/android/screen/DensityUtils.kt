package cn.yizems.util.ktx.android.screen

import android.content.Context
import android.util.TypedValue
import cn.yizems.util.ktx.android.context.getGlobalContext

fun Int.dp2px(): Float {
    return this.toFloat().dp2px()
}

fun Float.dp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, getGlobalContext().resources.displayMetrics
    )
}

fun Int.sp2px(): Float {
    return this.toFloat().sp2px()
}

fun Float.sp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this, getGlobalContext().resources.displayMetrics
    )
}

fun Int.px2dp(): Float {
    return this.toFloat().px2dp()
}

fun Float.px2dp(): Float {
    val scale = getGlobalContext().resources.displayMetrics.density
    return this / scale
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
