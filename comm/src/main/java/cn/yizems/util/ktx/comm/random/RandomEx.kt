package cn.yizems.util.ktx.comm.random

import java.util.*

/**
 * UUID 移除了 - 号
 */
fun uuid() =
    UUID.randomUUID().toString()
        .replace("-", "")
