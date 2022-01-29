package cn.yizems.util.ktx.comm.shell

/**
 * 给String扩展 execute() 函数
 */
fun String.execute(): Process {
    val runtime = Runtime.getRuntime()
    return runtime.exec(this)
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
    this.inputStream.bufferedReader()
        .forEachLine {
            println(it)
            out += it
            out += "\n"
        }
    return out
}
