package cn.yizems.util.ktx.android.screen

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.view.WindowManager
import cn.yizems.util.ktx.android.context.getGlobalContext


fun getScreenSize(): Point {
    val manager = getGlobalContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = manager.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point
}

fun getScreenRealSize(): Point {
    val manager = getGlobalContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = manager.defaultDisplay
    val point = Point()
    display.getRealSize(point)
    return point
}

fun getScreenRectSize(): Rect {
    val manager = getGlobalContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = manager.defaultDisplay
    val rect = Rect()
    display.getRectSize(rect)
    return rect
}

/**
 * 状态栏如果不存在,可能会得到0
 *
 * @return
 */
fun getStatusBarHeight(): Int {
    var result = 0
    val resourceId = getGlobalContext().resources
        .getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = getGlobalContext().resources.getDimensionPixelSize(resourceId)
    }
    return result
}
