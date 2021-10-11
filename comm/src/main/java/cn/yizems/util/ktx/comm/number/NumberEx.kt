package cn.yizems.util.ktx.comm.number

import cn.yizems.util.ktx.comm.type.getZeroAsEmpty
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * @param removeEndZero 是否移除多余的0
 * @param format 格式化,默认####.##
 */
fun Number?.formatMoney(removeEndZero: Boolean = true, format: String = "####0.00"): String {
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


fun Number?.toStringNullable(): String? {
    if (this == null) {
        return null
    }
    return this.toString()
}

/**
 * @receiver Number
 */
fun Number?.getZeroAsEmpty(blank: Boolean = true): String? =
    this.toStringNullable().getZeroAsEmpty(blank)