package cn.yizems.util.ktx.okhttp

import okhttp3.MediaType.Companion.toMediaType
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun mediaTypeTest() {
        println(MEDIA_TYPE_JSON_STR.toMediaType())
    }
}