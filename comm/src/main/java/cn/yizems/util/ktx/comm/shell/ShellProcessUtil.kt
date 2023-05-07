package cn.yizems.util.ktx.comm.shell

import cn.yizems.util.ktx.comm.file.toFile
import cn.yizems.util.ktx.comm.os.OS
import java.net.Inet6Address
import java.net.InetAddress

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
            val processInfo = listeningLine.substringAfterLast("LISTEN").trim().substringBefore(" ")
                .trim() // 24143/java

            return if (processInfo.contains("/")) {
                processInfo.split("/")[0]
            } else {
                processInfo
            }

        }

        error("不支持的操作系统")
    }

    /**
     * ping 命令
     * @param host 可以是域名，也可以是ip
     * @param forEachLine 每一行的回调
     * @return 返回ping的最终结果(所有)
     */
    fun ping(host: String, forEachLine: ((String) -> Unit)? = null): String {
        val ips = InetAddress.getAllByName(host)
        val sb = StringBuilder()
        sb.append("start ping $host").append("\n")
        forEachLine?.invoke(sb.toString())

        var log = ""
        if (ips.isEmpty()) {
            log = "$host not found,no ip find\n"
            forEachLine?.invoke(log)
            sb.append(log)
        }
        ips.forEach { ip ->
            log = "start ping ${ip.canonicalHostName}\n"

            sb.append(log)
            forEachLine?.invoke(log)

            // 地址是否可达
            InetAddress.getByName(ip.canonicalHostName).isReachable(500)
                .let {
                    log = "isReachable: $it\n"
                    forEachLine?.invoke(log)
                    sb.append(log)
                }

            val process = when {
                OS.isWindows() -> {
                    "ping ${ip.canonicalHostName}".execute()
                }

                ip is Inet6Address -> {
                    "ping6 ${ip.canonicalHostName} -c 5 -w 100".execute()
                }

                else -> {
                    "ping ${ip.canonicalHostName} -c 5 -w 100".execute()
                }
            }

            process.forEachLine {
                forEachLine?.invoke(it + "\n")
                sb.append(it + "\n")
            }

            if (process.waitFor() == 0) {
                log = "ping ${ip.canonicalHostName} success\n"
                forEachLine?.invoke(log)
                sb.append(log)
            } else {
                log = "ping ${ip.canonicalHostName} fail\n"
                forEachLine?.invoke(log)
                sb.append(log)
            }

            log = "ping ${ip.canonicalHostName} end\n-----------------\n"
            forEachLine?.invoke(log)
            sb.append(log)
        }
        return sb.toString()
    }
}
