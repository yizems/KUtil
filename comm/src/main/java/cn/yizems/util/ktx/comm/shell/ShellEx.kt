package cn.yizems.util.ktx.comm.shell

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
