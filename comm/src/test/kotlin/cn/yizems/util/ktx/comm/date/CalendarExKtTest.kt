package cn.yizems.util.ktx.comm.date

import org.junit.Test
import java.util.Calendar

class CalendarExKtTest {

    @Test
    fun testToYearStart() {
        val calendar = Calendar.getInstance().toFieldStartOrEnd(
            CalendarField.YEAR,
            true
        )
        assert(calendar.get(Calendar.YEAR) == 1)
        assert(calendar.get(Calendar.MONTH) == 0)
        assert(calendar.get(Calendar.DAY_OF_MONTH) == 1)
        assert(calendar.get(Calendar.HOUR_OF_DAY) == 0)
        assert(calendar.get(Calendar.MINUTE) == 0)
        assert(calendar.get(Calendar.SECOND) == 0)
        assert(calendar.get(Calendar.MILLISECOND) == 0)
    }

    @Test
    fun testToFieldStart() {
        val calendar = Calendar.getInstance().toFieldStartOrEnd(
            CalendarField.HOUR_OF_DAY,
            true
        )
        assert(calendar.get(Calendar.HOUR_OF_DAY) == 0)
        assert(calendar.get(Calendar.MINUTE) == 0)
        assert(calendar.get(Calendar.SECOND) == 0)
        assert(calendar.get(Calendar.MILLISECOND) == 0)
    }

    @Test
    fun testToFieldEnd() {
        val calendar = Calendar.getInstance().toFieldStartOrEnd(
            CalendarField.HOUR_OF_DAY,
            false
        )
        assert(calendar.get(Calendar.HOUR_OF_DAY) == 23)
        assert(calendar.get(Calendar.MINUTE) == 59)
        assert(calendar.get(Calendar.SECOND) == 59)
        assert(calendar.get(Calendar.MILLISECOND) == 999)
    }
}
