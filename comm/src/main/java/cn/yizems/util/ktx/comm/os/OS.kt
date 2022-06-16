package cn.yizems.util.ktx.comm.os

/**
 * 系统工具类
 */
object OS {
    fun getOSName(): String {
        return System.getProperty("os.name")
    }

    fun isWindows(): Boolean {
        return System.getProperty("os.name")?.contains("Windows", ignoreCase = true) ?: false
    }

    fun isLinux(): Boolean {
        return System.getProperty("os.name")?.contains("Linux", true) ?: false
    }
}
