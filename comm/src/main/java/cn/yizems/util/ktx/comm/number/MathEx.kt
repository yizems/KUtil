package cn.yizems.util.ktx.comm.number

import java.math.BigDecimal

/**
 * max() for BigDecimal
 *
 * @return 两个都为null, 返回 [BigDecimal.ZERO] , 否则返回大的那个
 */
fun maxNumber(v1: BigDecimal?, v2: BigDecimal?): BigDecimal {

    if (v1 == null && v2 == null) {
        return BigDecimal.ZERO
    }
    if (v1 == null) {
        return v2!!
    }
    if (v2 == null) {
        return v1
    }

    return if (v1 > v2) {
        v1
    } else {
        v2
    }
}
