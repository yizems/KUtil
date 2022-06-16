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

### InputStream 流操作

```kotlin
/**
 * 写入文件,并关闭流
 * 注意, 如果文件存在,会自动删除
 * 
 * @param file 文件
 */
fun InputStream.writeTo(file: File)
```

### reflect 反射

```kotlin
/**
 * 反射获取 lazy 属性 是否已经初始化
 */
fun <T> KProperty0<T>.isInitialized()
```

### MathEx 数学扩展

```kotlin
/**
 * max() for BigDecimal
 * 
 * @return 两个都为null, 返回 [BigDecimal.ZERO] , 否则返回大的那个
 */
fun maxNumber(v1: BigDecimal?, v2: BigDecimal?)
```

### NumberEx 数字扩展

```kotlin
/**
 * 数字格式化工具类
 * 如果为null, 返回空字符串
 *
 * @param removeEndZero 是否移除多余的0, 如果移除, 1.00 -> 1
 * @param format 格式化,默认###0.00
 */
fun Number?.formatStr(removeEndZero: Boolean = true, format: String = "####0.00")

/**
 * 将0.00 这样的字符串格式化为空字符串
 */
fun Number?.getZeroAsEmpty(blank: Boolean = true)

```

### OS 系统工具类

```kotlin
OS.getOSName()
OS.isWindows()
OS.isLinux()
```

### RandomEx 随机数

```kotlin
/**
 * UUID 移除了 - 号
 */
fun uuid()
```

### ShellEx.kt 命令行工具类

```kotlin
/**
 * 执行命令
 *
 * @param envp 环境变量
 * @param dir 工作目录
 */
fun Array<String>.execute(envp: Array<String>? = null, dir: File? = null):Process

/**
 * 执行命令
 *
 * @param envp 环境变量
 * @param dir 工作目录
 */
fun String.execute(envp: Array<String>? = null, dir: File? = null):Process

/**
 * 扩展Process扩展 text() 函数
 */
fun Process.text()

/**
 * 打印 Shell 执行结果
 */
fun Process.printOut()


```

### AnyEx.kt

```kotlin
/**
 * Null 时执行 block
 */
fun Any?.runOnNull(block: () -> Unit) 
```

### BooleanEx.kt 布尔值扩展
```kotlin

/**
 * true 时 执行 block
 * 
 * @return self
 */
inline fun Boolean?.onTrue(block: () -> Unit): Boolean?
/**
 * false 时 执行 block
 * 
 * @return self
 */
inline fun Boolean?.onFalse(block: () -> Unit): Boolean?
/**
 * null 时 执行 block
 * 
 * @return self
 */
inline fun Boolean?.onNull(block: () -> Unit): Boolean?
/**
 * 非 true 时执行block
 * 
 * @return self
 */
inline fun Boolean?.onNotTure(block: (Boolean?) -> Unit): Boolean?
/**
 * 非 false 时执行block
 * 
 * @return self
 */
inline fun Boolean?.onNotFalse(block: (Boolean?) -> Unit): Boolean?


/**
 * null 当做 true
 */
inline fun Boolean?.nullAsTrue(): Boolean
/**
 * null 当做 false
 */
inline fun Boolean?.nullAsFalse(): Boolean

/**
 * 转为 [Boolean], 兼容 [String] 和 [Number]
 *
 * [String] 支持的值: "true", "1", "0","yes","on",认为是true
 * null as false
 * @return Boolean
 */
fun Any?.toBooleanEx(): Boolean

/**
 * null as null
 * @see toBooleanEx
 */
fun Any?.toBooleanExNullable(): Boolean? 


```
