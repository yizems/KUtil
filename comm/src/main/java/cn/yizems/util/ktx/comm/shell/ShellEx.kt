package cn.yizems.util.ktx.comm.shell

import cn.yizems.util.ktx.comm.os.OS
import java.io.File
import java.nio.charset.Charset

/**
 * 执行命令
 *
 * @param envp 环境变量
 * @param dir 工作目录
 */
fun String.execute(envp: Array<String>? = null, dir: File? = null): Process {
    return Runtime.getRuntime()
        .exec(this, envp, dir)
}

/**
 * 执行命令
 *
 * @param envp 环境变量
 * @param dir 工作目录
 */
fun Array<String>.execute(envp: Array<String>? = null, dir: File? = null): Process {
    return Runtime.getRuntime()
        .exec(this, envp, dir)
}


/**
 * 扩展Process扩展 text() 函数
 */
fun Process.text(): String {
    // 输出 Shell 执行结果
    return this.inputStream.bufferedReader(if (OS.isWindows()) Charset.forName("GBK") else Charsets.UTF_8)
        .useLines {
            it.toList().joinToString(separator = "\n")
        }
}

fun <T> Process.useLines(block: (Sequence<String>) -> T) {
    return this.inputStream.bufferedReader(if (OS.isWindows()) Charset.forName("GBK") else Charsets.UTF_8)
        .useLines {
            block(it)
        }
}

fun <T> Process.forEachLine(block: (String) -> T) {
    return this.inputStream.bufferedReader(if (OS.isWindows()) Charset.forName("GBK") else Charsets.UTF_8)
        .forEachLine {
            block(it)
        }
}

/**
 * 打印 Shell 执行结果
 */
fun Process.printOut(): String {
    // 输出 Shell 执行结果
    var out = ""
    val reader =
        this.inputStream.bufferedReader(if (OS.isWindows()) Charset.forName("GBK") else Charsets.UTF_8)
    while (true) {
        val o = reader.readLine() ?: break
        println(o)
        out += o
        out += "\n"
    }
    return out
}
