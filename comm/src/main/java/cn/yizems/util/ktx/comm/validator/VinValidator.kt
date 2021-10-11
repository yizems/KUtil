package cn.yizems.util.ktx.comm.validator

/***
 * 验证VIN号第九位有效性
 * @property arrWeight Array<Int>  17位字符对应的权重
 *
 *规则：
 *车架号第9位是验证码，基于给定规则进行校验第9位是否符合规则。
 *不符合校验规则时，提示“车架号未通过第9位加验证码校验”，但不限制提交；审批
 *端针对不符合的车架号进行红字提示。
 *校验规则为：
 *其他16位字码对应数值乘以其所在位置的加权系数的和，除以11的余数为第9位的校
 *验码。
 *第9位校验码为0-9或X；当余数为10时，X为对应的校验码
 *
 */
object VinValidator {
    /**
     * 17位字符对应的权重  -1为第九位占位，不参与计算
     */
    private val arrWeight = arrayOf(8, 7, 6, 5, 4, 3, 2, 10, -1, 9, 8, 7, 6, 5, 4, 3, 2)

    /***
     * 获取vin每一位字符对应的数值 map
     * @return Map<String, String>
     */
    private fun getMapCorresponding(): Map<String, String> {
        val map = HashMap<String, String>()
        var i = 0
        // 0-9的数字对应的值为数字本身
        repeat(10) {
            map[i.toString()] = i.toString()
            i++
        }
        //A-Z的字母  A->ASCII：65  Z->ASCII：90
        var j = 0
        for (item in 65..90) {
            if (j > 8) j = 0
            j++
            if (item == 83) j = 2  //S->83
            if (item == 73 || item == 79 || item == 81) continue  //I->73 O->79 Q->81 这三个字母排除
            map[item.toChar().toString()] = j.toString()

        }
        return map
    }

    /***
     * 校验 VIN号
     * @param vin String vin号
     * @return Boolean  是否有效
     *
     *校验规则为：
     *其他16位字码对应数值乘以其所在位置的加权系数的和，除以11的余数为第9位的校
     *验码。
     *第9位校验码为0-9或X；当余数为10时，X为对应的校验码
     */
    fun validatorVin(vin: String): Boolean {
        if (vin.length != 17) return false
        var total = 0
        vin.forEachIndexed { index, c ->
            if (index != 8) {
                val map = getMapCorresponding()
                val num = (map[c.toString()] ?: "0").toInt() * arrWeight[index]
                total += num
            }
        }

        val result = if ((total % 11) == 10) "X" else (total % 11).toString()
        return result == vin[8].toString()
    }
}