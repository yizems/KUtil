package cn.yizems.util.ktx.comm.shell

import org.junit.Test

class ShellProcessUtilTest {

    @Test
    fun ping() {
        val result = ShellProcessUtil.ping("baidu.com") {
            print(it)
        }
//        println(result)
    }
}
