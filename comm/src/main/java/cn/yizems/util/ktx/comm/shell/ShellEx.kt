package cn.yizems.util.ktx.comm.shell

import java.io.File


fun String.execute(envp: Array<String>? = null, dir: File? = null): Process {
    return Runtime.getRuntime()
        .exec(this, envp, dir)
}

fun Array<String>.execute(envp: Array<String>? = null, dir: File? = null): Process {
    return Runtime.getRuntime()
        .exec(this, envp, dir)
}


/**
 * 扩展Process扩展 text() 函数
 */
fun Process.text(): String {
    // 输出 Shell 执行结果
    return this.inputStream.bufferedReader()
        .useLines {
            it.toList().joinToString(separator = "\n")
        }
}

fun Process.printOut(): String {
    // 输出 Shell 执行结果
    var out = ""
    val reader = this.inputStream.bufferedReader()
    while (true) {
        val o = reader.readLine() ?: break
        println(o)
        out += o
    }
    return out
}
