package cn.yizems.util.ktx.comm.type

import org.junit.Test

class BooleanExKtTest {

    class MyBoolean(
        var value: Boolean? = null,
        var valueString: String? = null
    ) {
        override fun toString(): String {
            return "MyBoolean(value=$value)"
        }
    }

    @Test
    fun toBooleanExNullable() {
        val t1 = MyBoolean()
        t1::value.setBooleanEx(BooleanType.FALSE)
        assert(t1.value == false)

        t1::value.setBooleanEx(BooleanType.TRUE)
        assert(t1.value == true)

        t1::valueString.setBooleanEx(BooleanType.TRUE)
        assert(t1.valueString == BooleanType.TRUE.value)

        t1::valueString.setBooleanEx(BooleanType.FALSE)
        assert(t1.valueString == BooleanType.FALSE.value)

        t1::valueString.setBooleanEx(BooleanType.TRUE_STRING)
        assert(t1.valueString == BooleanType.TRUE_STRING.value)


        t1::valueString.setBooleanEx(BooleanType.FALSE_STRING)
        assert(t1.valueString == BooleanType.FALSE_STRING.value)

        t1::valueString.setBooleanEx(BooleanType.YES)
        assert(t1.valueString == BooleanType.YES.value)

        t1::valueString.setBooleanEx(BooleanType.NO)
        assert(t1.valueString == BooleanType.NO.value)

        t1::valueString.setBooleanEx(BooleanType.ON)
        assert(t1.valueString == BooleanType.ON.value)

    }
}
