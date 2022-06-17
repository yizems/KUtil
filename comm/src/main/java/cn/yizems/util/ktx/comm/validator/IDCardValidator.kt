package cn.yizems.util.ktx.comm.validator

/**
 * 身份证工具类
 */
object IDCardValidator {

    /**
     * 校验身份证号码
     * @param idCardNum String? 身份证号码
     * @return String? 不为空代表出错误,返回错误提示信息:
     *      请输入身份证号码
     *      身份证号码长度不正确
     *      身份证号码有非法字符
     *      身份证号码中出生日期不合法
     *      身份证号码校验位错误
     */
    fun validate(idCardNum: String?): String? {
        idCardNum ?: return "请输入身份证号码"
        val vIdCardNum = idCardNum.replace("x", "X")

        if (vIdCardNum.length != 18) {
            return "身份证号码长度不正确！"
        }

        if (!"\\d{17}(\\d|X)".toRegex().matches(vIdCardNum)) {
            return "身份证号码有非法字符！"
        }

        if (!isDateCorrect(vIdCardNum)) {
            return "身份证号码中出生日期不合法！"
        }

        val checkBit = vIdCardNum.subSequence(17, 18)

        if (checkBit != getCheckBit(vIdCardNum)) {
            return "身份证号码校验位错误！"
        }

        return null
    }

    /** 生日是否正确 */
    private fun isDateCorrect(idCardNum: String): Boolean {
        /*非闰年天数*/
        val monthDayN = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        /*闰年天数*/
        val monthDayL = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val month = idCardNum.substring(10, 12).toInt()

        val day = idCardNum.substring(12, 14).toInt()

        if (month > 12 || month <= 0) {
            return false
        }

        val year = idCardNum.substring(6, 10).toInt()

        val isLeapyear = year % 4 == 0 && year % 100 != 0 || year % 400 == 0

        if (isLeapyear) {
            if (day > monthDayL[month - 1] || day <= 0) return false
        } else {
            if (day > monthDayN[month - 1] || day <= 0) return false
        }
        return true
    }

    /**
     * 获取校验位
     * @param idCardNum String
     * @return String
     */
    private fun getCheckBit(idCardNum: String): String {
        if (idCardNum.length < 17) return ""
        val checkTable = arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")
        val wi = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1)
        var sum = 0

        for (i in 0..16) {
            val ch = idCardNum.substring(i, i + 1)
            sum += ch.toInt() * wi[i]
        }
        val y = sum % 11
        return checkTable[y]
    }

    /**
     * 填充 校验位
     * @param idCardNum String 身份证前17位
     * @return String
     */
    fun fullCheckBit(idCardNum: String): String {
        return idCardNum.substring(0, 17).let {
            it + getCheckBit(it)
        }
    }
}
