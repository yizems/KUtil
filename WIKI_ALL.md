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

###  QQUtil 应用QQ工具类

```kotlin
/** 跳转到 QQ聊天 */
fun toChat(context: Context, no: String): Boolean
/**
 * 在跳转到QQ群页面前，需要先获取要跳转到QQ群的Key，
 * 获取Key的网址：https://qun.qq.com/join.html
 * @param key 由官网生成的key
 */
fun toGroup(context: Context, key: String): Boolean 
```

###  ScreenUtil.kt 屏幕工具类
```kotlin
/**
 * 获取屏幕宽高:已减去装饰,
 *
 * @see [android.view.Display.getSize]
 * 区别参照 [android.view.Display.getRealSize]
 *
 * @return Point
 */
@Suppress("DEPRECATION")
fun getScreenSize(): Point

/**
 * 获取屏幕真实宽高
 * @see android.view.Display.getRealSize
 * @return Point
 */
@Suppress("DEPRECATION")
fun getScreenRealSize(): Point

/**
 * Gets the size of the display as a rectangle, in pixels.
 *
 * @see android.view.Display.getRectSize
 * @return Rect
 */
@Suppress("DEPRECATION")
fun getScreenRectSize(): Rect

/**
 * 获取状态栏高度
 * 状态栏如果不存在,可能会得到0
 * @return
 */
fun getStatusBarHeight(): Int 

```

### ShareUtil 系统分享工具类

```kotlin
/**
 * 分享文本
 */
@JvmStatic
fun shareText(context: Context, text: String, title: String = DEFAULT_SHARE_TITLE) 
/** 分享一张图片 */
@JvmStatic
fun shareImage(context: Context, file: File, title: String = DEFAULT_SHARE_TITLE) 
/**
 * 分享多张图片
 */
@JvmStatic
fun shareMulImage(
    context: Context,
    files: ArrayList<File>,
    title: String = DEFAULT_SHARE_TITLE
)
/** 分享一个文件 */
@JvmStatic
fun shareFile(
    context: Context, file: File,
    mime: String = "*/*",
    title: String = DEFAULT_SHARE_TITLE
) 
/** 分享多个文件 */
@JvmStatic
fun shareMulFile(
    context: Context, files: ArrayList<File>,
    mime: String = "*/*",
    title: String = DEFAULT_SHARE_TITLE
)
```

### FileUri 文件Uri 工具类

方面获取文件Uri的一些信息

```kotlin

/**
 * 获取File的Uri,兼容Android 7 的fileProvider
 */
inline fun File.getUri(context: Context): Uri



/**
 * 文件信息
 * @property name 文件名
 * @property uri 文件uri
 * @property length 文件大小
 */
data class FileInfo


/**
 * 获取文件信息,主要是外部的文件uri
 * @receiver Context
 * @param uriString String
 * @return FileInfo
 */
fun Context.getFileInfo(uriString: String): FileInfo

/**
 * 获取文件信息,主要是外部的文件uri
 *
 * @receiver Context
 * @param uri Uri
 * @return FileInfo
 */
fun Context.getFileInfo(uri: Uri): FileInfo

```

### DecimalInputFilter 小数输入框处理器


```kotlin
/**
 * 设置为小数输入格式
 * 金额输入过滤器
 * 高仿微信转账规则
 *
 * 可以精确控制整数位和小数位位置
 *
 * 配合[setTextSkipDecimalInputFilter] 可以设置进去不符合规则的数值
 *
 * 对于 负数,这里直接跳过,不做处理,负数默认为设置进去的值,而不是用户手动录入的值
 *
 *
 * @param prefix 整数位
 * @param suffix 小数位
 */
fun EditText.setDecimalStyle(prefix: Int = 7, suffix: Int = 2)

/**
 * 设置文字跳过 [setDecimalStyle] 的处理
 */
fun EditText.setTextSkipDecimalInputFilter(text: String)
```

### TextViewEx TextView 和 EditTextEx 常用扩展

```kotlin
/** 是否为空, 空白字符也认为是空 */
fun TextView.isEmpty(): Boolean

/** 非空 */
fun TextView.isNotEmpty(): Boolean

/** trim 过的文本 */
fun TextView.getTrimmedText(): String

/** 设置最大输入长度 */
fun TextView.setMaxLengths(max: Int)

/** 获取double值 */
fun TextView.getDouble(): Double?

/** 获取double值, 如果转换失败或为空, 则返回 [default] */
fun TextView.getDoubleOrElse(default: Double = 0.0): Double

/** 获取 int 值 */
fun TextView.getInt(): Int

/** 获取int值, 如果转换失败或为空, 则返回 [default] */
fun TextView.getIntOrElse(default: Int = 0): Int

/**  为空或者只有`Blank`时返回默认值: [default] 不可为null */
fun TextView.getOrElse(default: String): String

/**  为空或者只有`Blank`时返回默认值: [default] 可为 null */
fun TextView.getOrElseNullable(default: String?): String?
fun TextView.clear()
fun EditText.clear()

/**
 * 是否只读
 */
var EditText.readonly: Boolean

/**
 * 字符转大写
 * @receiver TextView
 */
fun TextView.allCaps()

/**
 * 取消字符转大写
 * @receiver TextView
 */
fun TextView.removeAllCaps()

/**
 * 数据变化了的监听
 */
fun TextView.setOnTextChangedListener(onTextChanged: () -> Unit)
```

### EditTextEx 扩展类, EditTextEx特有方法

```kotlin
/**
 * 整数型,有焦点后 如果内容为0,删除,失去焦点,如果内容为空,自动填充0
 */
fun EditText.autoRemoveZeroOnFocused()

/**
 * 焦点消失时,数据变化了的监听
 */
fun EditText.setFocusMissDataChangedListener(onChanged: (oldStr: String, newStr: String) -> Unit)

/** 添加焦点监听事件: 可以添加多次,监听按照添加顺序调用 */
fun EditText.addFocusChangeListener(focusChangedListener: View.OnFocusChangeListener)

/** 移除焦点监听事件 */
fun EditText.removeFocusChangeListener(focusChangedListener: View.OnFocusChangeListener)

```


### InnerListView 可嵌套的ListView

```xml
<cn.yizems.util.ktx.android.view.listview.InnerListView
    android:id="@+id/listView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:max_percent_height="0.5" />

```
`max_percent_height: 用于控制最大高度占屏幕的多少, 一般多用于弹窗, 如果设置为0,则不限制高度

### InnerScrollView 可嵌套的ScrollView 用法和 `InnerListView`一样

### ColorLinearLayoutItemDecoration RecycleView.LinearLayoutManager 但颜色分割线

```kotlin
/**
 * RecycleView.LinearLayoutManager 单颜色分割线
 * 
 * @param startPaddingPx 开头间距
 * @param endPaddingPx 结尾间距
 * @param dividerHeightPx 分割线高度
 * @param dividerColor 分割线颜色
 * @param skipPosition 是否跳过某个位置
 */
class ColorLinearLayoutItemDecoration(
    private val startPaddingPx: Float = 0F,
    private val endPaddingPx: Float = 0F,
    private val dividerHeightPx: Float = 0.5F.dp2px(),
    private val dividerColor: Int,
    private val skipPosition: ((position: Int) -> Boolean)? = null
) : RecyclerView.ItemDecoration()
```

### ViewShot 截图相关

**来源于网络,出处已不知**

```kotlin
object ViewShot {
    /**
     * 手动测量摆放View
     * 对于手动 inflate 或者其他方式代码生成加载的View进行测量，避免该View无尺寸
     * @param v
     * @param width
     * @param height
     */
    fun layoutView(v: View, width: Int, height: Int)
    /**
     * 获取一个 View 的缓存视图
     * (前提是这个View已经渲染完成显示在页面上)
     * @param view
     * @return
     */
    fun getCacheBitmapFromView(view: View): Bitmap?
    /**
     * 对ScrollView进行截图
     * 
     * @param scrollView
     * @return
     */
    fun shotScrollView(scrollView: ScrollView): Bitmap?
    /**
     * 对ListView进行截图
     * 
     * http://stackoverflow.com/questions/12742343/android-get-screenshot-of-all-listview-items
     */
    fun shotListView(listview: ListView): Bitmap
    /**
     * 对RecyclerView进行截图
     * 
     * https://gist.github.com/PrashamTrivedi/809d2541776c8c141d9a
     */
    fun shotRecyclerView(view: RecyclerView): Bitmap?

```

### DeviceInfo.kt 设备信息

```kotlin
/**
 * 获取AndroidId
 * @see Settings.Secure.ANDROID_ID
 */
fun getAndroidId(): String 
```


### ViewPager2 相关

#### 滑动冲突解决: `ViewPager2NestedScrollableHost`

```kotlin
/**
 * Layout to wrap a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2. The scrollable element needs to be the immediate and only child of this host layout.
 *
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 *
 * e.g.  https://github.com/android/views-widgets-samples/blob/main/ViewPager2/app/src/main/res/layout/item_nested_recyclerviews.xml
 *      //没必要在根节点, [parentViewPager] 会一直向上搜索ViewPager2 节点
 *      <androidx.viewpager2.integration.testapp.NestedScrollableHost
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content"
 *          android:layout_marginTop="8dp">
 *          <androidx.recyclerview.widget.RecyclerView
 *              android:id="@+id/first_rv"
 *              android:layout_width="match_parent"
 *              android:layout_height="wrap_content"
 *              android:background="#FFFFFF" />
 *      </androidx.viewpager2.integration.testapp.NestedScrollableHost>
 *
 *  官方解决的是 VP2 嵌套 滑动方向一致的 子View 时,子View 无法响应触摸的问题
 *
 * 修复 ViewPager2 嵌套WebView/其他 和 VP2 方向不一样时,滑动不灵敏的问题
 */

class ViewPager2NestedScrollableHost : FrameLayout
```

#### SafePagerAdapter 和 IFragmentItem

`SafePagerAdapter` 主要是用来解决`Fragment`使用不规范的问题,比如: 直接 new Fragment 然后传给 PagerAdapter

```kotlin
/**
 * 更加安全的 adapter 实现, 配合 [IFragmentItem] 使用
 *
 * @property items List<IFragmentItem>
 */
class SafePagerAdapter : FragmentStateAdapter {

    private val items: List<IFragmentItem>

    constructor(fragmentActivity: FragmentActivity, items: List<IFragmentItem>)
            : super(fragmentActivity) {
        this.items = items
    }

    constructor(fragment: Fragment, items: List<IFragmentItem>) : super(fragment) {
        this.items = items
    }

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        items: List<IFragmentItem>
    ) : super(fragmentManager, lifecycle) {
        this.items = items
    }
}
```

`IFragmentItem` 核心逻辑是定义了 创建 fragment 的代码. 并含有子类用于实现 图标或者标题的设置

```kotlin
/**
 * fragment item
 * 配合 [SafePagerAdapter] 使用
 *
 * warn: 不要使用匿名内部类!!!不要使用匿名内部类!!!不要使用匿名内部类!!!
 */
interface IFragmentItem {
    fun create(position: Int): Fragment
}

interface IconFragmentItem : IFragmentItem {
    val selIcon: Int
    val defaultIcon: Int
}

/** 标题 , title 为 字符串 id */
interface TitleResFragmentItem : IFragmentItem {
    val title: Int
}

interface TitleFragmentItem : IFragmentItem {
    val title: String
}

```

### ViewEx 扩展

```kotlin
/**
 * 是对于 ktx 的补充, ktx 中有的方法,这里没有实现
 */

/**
 * [doOnPreDraw] 的扩展,action 返回值决定是否取消绘制
 * @receiver View
 * @param action Function1<[@kotlin.ParameterName] View, Boolean> 返回true,代表继续绘制
 */
inline fun View.doOnPreDrawEx(crossinline action: (view: View) -> Boolean)

/**
 * 获取焦点
 * 会设置 [View.isFocusableInTouchMode] [View.isEnabled] 为 true
 */
fun View.requestFocusEx()

/**
 * 设置 View 的 margin
 */
inline fun View.updateMargins(
    @Px left: Int? = null,
    @Px top: Int? = null,
    @Px right: Int? = null,
    @Px bottom: Int? = null
)
```

### 协程支持: `CoroutineSupport`

**强烈建议阅读源码后再使用, 下面是示例**

```kotlin
package cn.yizems.util.ktx.android.coroutine.demo

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import cn.yizems.util.ktx.android.coroutine.CoroutineSupport
import cn.yizems.util.ktx.android.coroutine.ILoadingSupport
import cn.yizems.util.ktx.android.coroutine.ToastException
import cn.yizems.util.ktx.android.coroutine.ToastProvider
import kotlinx.coroutines.*

private class BaseActivity
    : AppCompatActivity(), CoroutineSupport, ILoadingSupport {
    /**
     * 默认主线程, 子类需要实现
     */
    override val coroutineScope: CoroutineScope
        get() = lifecycle.coroutineScope

    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    override fun showLoading(msg: String?) {
        // 显示弹窗
    }

    override fun dismissLoading() {
        // 关闭弹窗
    }


    private fun biz() {
        // 具体业务逻辑
        launch {
            delay(5_000)
            ToastProvider.showShort("业务完成")
        }
        launch(showHint = true) {

            onError {
                if (it is IllegalArgumentException) {
                    // 返回 true, 这种异常会被拦截, 需要你自己处理这里的逻辑, 不会 toast 提示
                    return@onError true
                }
                // 返回 false, 代表不拦截
                return@onError false
            }

            delay(5_000)

            // 这种类型的异常,以及子类, 会被外部捕获, 并自动toast出来
            // 和 [showHint] 参数有关, 如果设置为false, 则不会 toast 提示
            throw ToastException("业务异常")
        }
    }

}

private class BasePresenter(private val mView: Any?) : CoroutineSupport, ILoadingSupport {

    /**
     * 默认主线程, 子类需要实现
     */
    override val coroutineScope: CoroutineScope
        get() = CoroutineScope((SupervisorJob() + Dispatchers.Main))

    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    override fun showLoading(msg: String?) {
//        mView?.showLoading(msg)
    }

    override fun dismissLoading() {
//        mView?.dismissLoading()
    }

    fun detach() {
        coroutineScope.cancel()
    }
}

private class BaseDialog(context: Context, private val lifecycle: Lifecycle) : Dialog(context),
    CoroutineSupport, ILoadingSupport {


    /**
     * 默认主线程, 子类需要实现
     */
    override val coroutineScope: CoroutineScope
        get() = lifecycle.coroutineScope


    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    override fun showLoading(msg: String?) {
        // 显示dialog中的loading视图
    }

    override fun dismissLoading() {
        // 隐藏dialog中的loading视图
    }

    override fun dismiss() {
        super.dismiss()
    }
}

```


## okhttp 扩展库 - Java项目

```kotlin

```
