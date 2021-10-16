package cn.yizems.util.ktx.comm.random

import java.util.*


fun uuid() =
    UUID.randomUUID().toString()
        .replace("-", "")
