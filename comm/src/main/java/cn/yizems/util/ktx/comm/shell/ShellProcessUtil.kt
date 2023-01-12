package cn.yizems.util.ktx.comm.shell

import cn.yizems.util.ktx.comm.file.toFile
import cn.yizems.util.ktx.comm.os.OS

/**
 * 命令行进程工具类
 */
object ShellProcessUtil {
    /**
     * 使用 jps -l 命令获取进程id
     */
    fun findProcessIdByJps(jarName: String): String? {
        val ret = "jps -l".execute().text().split("\n")
        return ret.firstOrNull { it.contains(jarName) }?.split(" ")?.get(0)
    }

    /**
     * 根据端口占用情况，获取进程id
     * netstat命令
     */
    fun findProcessIdByPort(port: String): String? {
        val userDir = System.getProperty("user.home").toFile()

        if (OS.isWindows()) {
            val lines =
                "cmd /c netstat -aon|findstr $port".execute(dir = userDir).text().split("\n")
            //TCP    127.0.0.1:5601         0.0.0.0:0              LISTENING       23028
            val listeningLine = lines.firstOrNull { it.contains("LISTENING") } ?: return null
            return listeningLine.substringAfterLast("LISTENING").trim()
        }

        if (OS.isLinux()) {
            val lines = "netstat -anop | grep $port".execute(dir = userDir).text().split("\n")
            val listeningLine = lines.firstOrNull { it.contains("LISTEN") } ?: return null
            //tcp        0      0 0.0.0.0:8080            0.0.0.0:*               LISTEN      24143/java           off (0.00/0/0)
            val processInfo = listeningLine.substringAfterLast("LISTEN").trim()
                .substringBefore(" ")
                .trim() // 24143/java

            return if (processInfo.contains("/")) {
                processInfo.split("/")[0]
            } else {
                processInfo
            }

        }

        error("不支持的操作系统")
    }
}
