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

### ResultEx: runCatching 扩展

```kotlin

kotlin.runCatching {
    //do something
}.toResultWrapper() // 转为加强类
    .onFailureWith<IllegalAccessException> { // 当为IllegalAccessException异常时执行

    }.onFailureWith<IllegalArgumentException> { // 当为 IllegalArgumentException 异常时执行

    }

/**
 * 异常处理: 带类型, 无异常则不执行
 *
 * 如果 [block] 返回true, 则后续的异常不会再处理
 *
 * @receiver ResultWrapper<T>
 * @return ResultWrapper<T>
 */
inline fun <T, reified R> ResultWrapper<T>.onFailureWith(block: (Throwable) -> Boolean): ResultWrapper<T>

```

### StringEx.kt

```kotlin
/**
 * null 时 返回 [other]
 */
infix fun <T : CharSequence?> CharSequence?.nullOr(other: T): T

/**
 * null 时 返回 [others] 中不为null的值,都为 null 则返回 null
 */
fun <T : CharSequence?> CharSequence?.nullOr(vararg others: T): T

/**
 * empty 时 返回 [other]
 */
infix fun <T : CharSequence?> CharSequence?.emptyOr(other: T): T

/**
 * empty 时 返回 [others] 不为空的值
 */
fun <T : CharSequence?> CharSequence?.emptyOr(vararg others: T): T

/**
 * blank 时返回 [other]
 */
infix fun <T : CharSequence?> CharSequence?.blankOr(other: T): T
if (this == ".") {
    return 0.0
}
return this.toDouble()

/** blank 时 返回 [others] 不为空的值 */
fun <T : CharSequence?> CharSequence?.blankOr(vararg others: T): T

/**
 * 删除结尾 .000
 * :2018-02-02 12:00:00.000
 * @receiver String
 * @param default String
 * @return String
 */
fun String?.dropDatePoint(): String

/** 转为double时, null->null, '.'->0.0 */
fun String?.toDoubleEx(): Double?

/**
 * 转为double,如果出错或为null , 返回 [default]
 */
fun String?.toDoubleOrElse(default: Double = 0.0, ignoreException: Boolean = true): Double

/** 转为int, 如果异常或为null,返回 [default] */
fun String?.toIntOrElse(default: Int = 0, exception: Boolean = false): Int

/**
 * 针对 0.00 0 这样的数据,当做空"" 字符串处理,
 * @param blank String 0 的话是否返回 ""
 */
fun String?.getZeroAsEmpty(blank: Boolean = true): String?

```

### IDCardValidator 身份证校验

```kotlin
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
    fun validate(idCardNum: String?): String?
}
```

### VinValidator 车架号校验

```kotlin
// return 是否符合Vin 号校验规则
VinValidator.validatorVin(vin: String): Boolean
```

## android-context 安卓Context 工具类

### ContextHolder.kt 引入即可全局获取 Context

```kotlin
// 获取全局Context
getGlobalContext()
// 获取Application
getApplication()
```

## android-core 安卓核心库扩展

### ActivityEx.kt

```kotlin
/** 关闭软键盘 */
fun Activity.hideSoftInput()

/**
 * 是否正在显示软键盘
 */
fun Activity.isSoftShowing(): Boolean 
```

### ActivityStackManager 全局Activity管理

```kotlin
object ActivityStackManager {

    /** 在Application.onCreate()中先注册监听 */
    fun registerCallback(application: Application)

    /**
     * 获取一份 当前存活的activity list 的 copy
     */
    @Synchronized
    fun getList(): List<Activity>

    /**
     * 注册全局 lifeCycle 监听
     */
    fun addListener(event: Lifecycle.Event, callback: LifeCallback)

    /** 移除监听 */
    fun removeListener(event: Lifecycle.Event, callback: LifeCallback)

    /**
     * 移除[event]所有监听
     */
    fun removeListener(event: Lifecycle.Event)
}
```

### AnimationEx

```kotlin
/**
 * 应用动画
 * @param anim 动画资源id
 */
fun View.startAnimationEx(@AnimRes anim: Int)
```

### ApkUtil APK 工具类

```kotlin
/**
 * APK 工具类
 */
class ApkUtil {
    /**
     * 安装APK
     * @param downloadApk 文件路径
     */
    fun installApk(downloadApk: String, context: Context? = null)
}
```

### AppSignUtil APP 签名工具类

```kotlin
object AppSignUtil {
    /** 获取应用签名 */
    @Suppress("DEPRECATION")
    fun getSign(packageName: String = getGlobalContext().packageName): Array<out Signature>?

    /** 获取应用签名Md5 */
    fun getSignMD5(packageName: String = getGlobalContext().packageName): String?

    /** 获取应用签名Sha1 */
    fun getSignSHA1(packageName: String = getGlobalContext().packageName): String? {
        val sign = getSign(packageName) ?: return null
        return sign.firstOrNull()?.toByteArray()?.let {
            it.toByteString(0, it.size)
        }?.sha1()?.utf8()
    }
    /** 获取某一个应用的MetaData */
    /** 获取应用签名Sha256 */
    fun getSignSHA256(packageName: String = getGlobalContext().packageName): String?

    @SuppressLint("WrongConstant")
    @Suppress("DEPRECATION")
    fun getMetaData(packageName: String = getGlobalContext().packageName): Bundle?
}
```

### ContextEx.kt 安卓Context 工具类

```kotlin
/** 获取当前应用的版本号 */
val Context.appVersionNo: Long

/** 获取当前应用的版本名 */
val Context.appVersionName: String

/** 获取当前应用的 [PackageInfo] */
fun Context.getPackageInfo(flag: Int = 0, packageName: String = this.packageName)

```

### DemensEx.kt 尺寸快捷方法

```kotlin
fun Int.dp2px(): Float
fun Float.dp2px(): Float
fun Int.sp2px(): Float
fun Float.sp2px(): Float
fun Int.px2dp(): Float
fun Float.px2dp(): Float
```

### FragmentEx 安卓Fragment 工具类

```kotlin
/**
 * 添加 fragment 并 根据 [show] 设置最大生命周期
 *
 * @receiver FragmentTransaction
 * @param fragment Fragment
 * @param containerViewId Int
 * @param tag String?
 * @param show 是否默认显示
 * @param lifeState State 最大生命周期, 一般来说不需要设置, 根据[show]指定判定就好
 * @return FragmentTransaction
 */
fun FragmentTransaction.addFragmentAndSetLife(
    fragment: Fragment,
    @IdRes containerViewId: Int = 0,
    tag: String? = null,
    show: Boolean = false,
    lifeState: Lifecycle.State = if (show) Lifecycle.State.RESUMED else Lifecycle.State.STARTED,
): FragmentTransaction

/**
 * 显示frgment
 * 并将其他fragment 隐藏
 * 调整显示的和隐藏的fragment的MaxLifecycle
 * @receiver FragmentManager
 * @param fragment Fragment
 */
fun FragmentManager.showFragment(fragment: Fragment)
fun FragmentManager.showFragmentByTag(tag: String)

/**
 * 将fragment 隐藏
 * 调整隐藏的fragment的MaxLifecycle 到 [Lifecycle.State.STARTED]
 * @receiver FragmentManager
 * @param fragment Fragment
 */
fun FragmentManager.hideFragment(fragment: Fragment)
fun FragmentManager.hideFragmentByTag(tag: String)

/**
 * 显示fragment 并调整fragment 到 [Lifecycle.State.RESUMED]
 * @receiver FragmentTransaction
 * @param fragment Fragment
 * @return FragmentTransaction
 */
fun FragmentTransaction.showAndMoveLife(fragment: Fragment): FragmentTransaction

/**
 * 隐藏fragment 并调整fragment 到 [Lifecycle.State.STARTED]
 * @receiver FragmentTransaction
 * @param fragment Fragment
 * @return FragmentTransaction
 */
fun FragmentTransaction.hideAndMoveLife(fragment: Fragment): FragmentTransaction

```

### HandlerEx.kt 安卓Handler 工具类

```kotlin
/** 判断当前线程是否是主线程 */
fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()

/** 获取一个懒加载的 主线程的 handler */
val mainHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    Handler(Looper.getMainLooper())
}
```

### SoftKeyBoardListener 安卓软键盘监听器

```kotlin

class SoftKeyBoardListener {
    companion object {
        /**
         * @param activity Activity
         * @param hide ((Int) -> Unit)? 隐藏监听
         * @param show ((Int) -> Unit)? 显示监听
         */
        @JvmStatic
        fun setListener(
            activity: Activity,
            hide: ((Int) -> Unit)? = null,
            show: ((Int) -> Unit)? = null
        )
    }
}
```

### MediaStoreRepository 媒体库数据库操作类

```kotlin
object MediaStoreRepository {
    /**
     * 插入文件
     * @param dir String 插入到哪个目录: like [Environment.DIRECTORY_PICTURES]
     */
    @Suppress("DEPRECATION")
    suspend fun insertFile(dir: String, file: File, title: String? = null)

    /**
     * 插入图片到指定文件夹
     * @param title 不需要携带 后缀, 会自动根据 [format] 自动添加
     * @param format bitmap 转换格式, 只支持 JPEG,PNG,WEBP
     */
    @Suppress("DEPRECATION")
    suspend fun insertBitmap(
        dir: String,
        bitmap: Bitmap,
        title: String = uuid(),
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    ): Boolean
}
```

### ProcessUtil 应用进程工具类

```kotlin
object ProcessUtil {
    /** 获取当前进程名字 */
    fun getCurrentProcessName(context: Context): String?

    /** 当前是否在主进程 */
    fun isMainProcess(context: Context): Boolean
}
```

###  
