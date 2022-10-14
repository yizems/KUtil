package cn.yizems.util.ktx.android.coroutine

object CoroutineSupportManager {

    private var logPrinter: ILogPrinter = DefaultLogPrinter()

    private var toastProvider: IToastProvider? = null

    fun register(toastProvider: IToastProvider) {
        this.toastProvider = toastProvider
    }

    fun setLogPrinter(logPrinter: ILogPrinter) {
        this.logPrinter = logPrinter
    }

    fun getToastProvider(): IToastProvider? {
        return toastProvider
    }

    fun getLogPrinter(): ILogPrinter {
        return logPrinter
    }
}
