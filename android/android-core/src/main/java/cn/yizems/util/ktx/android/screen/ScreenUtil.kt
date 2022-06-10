package cn.yizems.util.ktx.android.screen

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.view.WindowManager
import cn.yizems.util.ktx.android.context.getGlobalContext

/**
 * 获取屏幕宽高:已减去装饰,
 *
 * @see [android.view.Display.getSize]
 * 区别参照 [android.view.Display.getRealSize]
 *
 * @return Point
 */
@Suppress("DEPRECATION")
fun getScreenSize(): Point {
    val manager = getGlobalContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = manager.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point
}

/**
 * 获取屏幕真实宽高
 * @see android.view.Display.getRealSize
 * @return Point
 */
@Suppress("DEPRECATION")
fun getScreenRealSize(): Point {
    val manager = getGlobalContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = manager.defaultDisplay
    val point = Point()
    display.getRealSize(point)
    return point
}

/**
 * Gets the size of the display as a rectangle, in pixels.
 *
 * @see android.view.Display.getRectSize
 * @return Rect
 */
@Suppress("DEPRECATION")
fun getScreenRectSize(): Rect {
    val manager = getGlobalContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = manager.defaultDisplay
    val rect = Rect()
    display.getRectSize(rect)
    return rect
}

/**
 * 获取状态栏高度
 * 状态栏如果不存在,可能会得到0
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
