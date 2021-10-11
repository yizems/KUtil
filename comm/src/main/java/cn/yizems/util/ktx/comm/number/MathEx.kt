package cn.yizems.util.ktx.comm.number

import java.math.BigDecimal

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

    if (v1 > v2) {
        return v1
    } else {
        return v2
    }
}