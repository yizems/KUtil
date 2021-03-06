package cn.yizems.util.ktx.comm.collection

/**
 * 集合工具类
 */


/**
 * 获取不被 elements 包含的元素,
 * 返回空list则表示 全部包含
 */
inline fun <T, R> List<T>.noContainElementBy(
    elements: List<R>,
    condition: (item: T, element: R) -> Boolean
): List<R> {
    val noContain = ArrayList<R>()
    elements.forEach { element ->
        if (this.none { condition(it, element) }) {
            noContain.add(element)
        }
    }
    return noContain
}

/**
 * 获取被包含的元素,
 * 返回被 elements 包含的条目
 * 返回空list则表示 没有包含的
 */
inline fun <T, R> List<T>.containElementBy(
    elements: List<R>,
    condition: (item: T, element: R) -> Boolean
): List<R> {
    val contain = ArrayList<R>()
    elements.forEach { element ->
        if (this.filter { condition(it, element) }.isNotEmpty()) {
            contain.add(element)
        }
    }
    return contain
}

fun List<Any>?.isNullOrEmpty(): Boolean {
    if (this == null) return true
    return this.isEmpty()
}