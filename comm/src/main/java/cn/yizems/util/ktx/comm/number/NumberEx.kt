package cn.yizems.util.ktx.comm.number

import cn.yizems.util.ktx.comm.type.getZeroAsEmpty
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * 数字格式化工具类
 * 如果为null, 返回空字符串
 *
 * @param removeEndZero 是否移除多余的0, 如果移除, 1.00 -> 1
 * @param format 格式化,默认###0.00
 */
fun Number?.formatStr(removeEndZero: Boolean = true, format: String = "####0.00"): String {
    if (this == null) {
        return ""
    }
    val df = DecimalFormat(format)
    df.roundingMode = RoundingMode.HALF_UP
    var result = df.format(this)
    if (removeEndZero) {
        while (result.contains(".") && (result.endsWith("0") || result.endsWith("."))) {
            result = result.dropLast(1)
        }
    }
    return result
}

@Deprecated("过时", ReplaceWith("formatStr(removeEndZero, format)"))
fun Number?.formatMoney(removeEndZero: Boolean = true, format: String = "####0.00") =
    formatStr(removeEndZero, format)


/**
 * 将0.00 这样的字符串格式化为空字符串
 */
fun Number?.getZeroAsEmpty(blank: Boolean = true): String? =
    this?.toString().getZeroAsEmpty(blank)
