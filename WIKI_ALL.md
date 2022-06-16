## Comm 模块

### collection 集合操作

```kotlin

val a = listOf("a","b")

val b = listOf(1)

// 返回不包含的元素,支持不同数据类型
b.noContainElementBy(a){
    1.toString()==b
}

// 获取被包含的元素
b.containElementBy(a){
    1.toString()==b
}

// 判空
val c:List<String>? = null

val empty = c.isNullOrEmpty()

```

### DisgestEncoder 数字摘要

```kotlin

// 字符串方法

fun String.md5()
fun String.sha1()
fun String.sha256()
fun String.sha512()
fun String.hex()
fun String.base64()
fun String.base64Url()

// 文件方法

fun File.md5()
fun File.sha1()
fun File.sha256()
fun File.sha512()
fun File.base64()

```

### CalendarEx 日期

```kotlin

// 设置属性
fun Calendar.setFieldEx(
    year: Int? = null,
    month: Int? = null,
    day: Int? = null,
    hourOfDay: Int? = null,
    minute: Int? = null,
    second: Int? = null,
    millions: Int? = null,
)
// 设置某个时间的开始或结束: 例如 今天的开始时间, 0时0分0秒0毫秒
fun Calendar.toFieldStartOrEnd(
    cField: CalendarField = CalendarField.YEAR,
    start: Boolean,
)

enum class CalendarField(val field: Int) {
    YEAR(Calendar.YEAR),
    MONTH(Calendar.MONTH),
    DAY_OF_MONTH(Calendar.DAY_OF_MONTH),
    HOUR_OF_DAY(Calendar.HOUR_OF_DAY),
    MINUTE(Calendar.MINUTE),
    SECOND(Calendar.SECOND),
    MILLISECOND(Calendar.MILLISECOND),
    ;
}
// 格式化方法
fun Calendar.format(format: String = "yyyy-MM-dd HH:mm:ss.SSS")

```

### Throwable 异常

获取异常信息字符串
`fun Throwable?.toLogString()`

### FileEx 文件操作

```
// 路径转文件
inline fun String.toFile()

// 获取一个随机文件名,但是并没有重命名
fun File.generateUuidName()

/**
 * 重命名
 * @receiver File
 * @param name String 新文件名
 * @param renameSuffix Boolean 如果为false 则 [name] 包含后缀名,否则不包含
 */
fun File.rename(name: String, renameSuffix: Boolean = false)

/**
 * 防止部分情况下获取父文件[File] 为空的情况: 安卓中常见
 *
 * @receiver File
 * @return File
 */
inline fun File.parentFileCompat()

/**
 * 兄弟文件
 * @receiver File
 * @param name String
 */
inline fun File.brother(name: String)

/**
 * 子文件
 * @receiver File
 * @param name String
 */
inline fun File.child(name: String)

/**
 * 获取文件夹大小, 单位B
 */
fun File.getDirSize()

/**
 * 格式化单位
 * ex: 1024 -> 1K, 1024000 -> 1M
 */
fun File.getFormatSize()

/**
 * 获取文件MD5
 * 
 * @return md5, 文件不存在返回 null
 */
fun File.md5()


/**
 * 删除目录
 * 
 * @param deleteThisPath 是否删除目录
 */
fun File.deleteDir(
    deleteThisPath: Boolean = false
) 

/**
 * 写入文件,并同事关闭流
 * 
 * @param path 文件路径
 */
fun InputStream.writeToFile(path: String)

```

