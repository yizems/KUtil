package cn.yizems.util.ktx.android.permission

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import java.security.InvalidParameterException

/**
 * 权限申请工具类
 * 结果有3种情况
 * 1. 同意,直接执行后续逻辑
 * 2. 拒绝,但是没有点击不再询问, 显示弹窗,按钮(取消,再次执行),执行后续逻辑
 * 3. 不再询问: 显示弹窗,按钮(取消,去设置),不会自动执行后续逻辑
 */
object PermissionUtil {

    //region 快捷扩展方法


    /**
     * 扩展函数: 快速申请权限
     * @receiver FragmentActivity
     * @param permission Array<out String>
     * @param permissionGrantedCallback Function0<Unit>
     */
    fun FragmentActivity.reqPermission(
        vararg permission: String,
        permissionGrantedCallback: () -> Unit
    ) {
        requestPermission(this, arrayOf(*permission), permissionGrantedCallback)
    }

    /**
     * 扩展函数: 快速申请权限
     * @receiver Fragment
     * @param permission Array<out String>
     * @param permissionGrantedCallback Function0<Unit>
     */
    fun Fragment.reqPermission(
        vararg permission: String,
        permissionGrantedCallback: () -> Unit
    ) {
        requestPermission(this, arrayOf(*permission), permissionGrantedCallback)
    }

    /**
     * 申请 camera 和 read external storage 权限
     * @receiver FragmentActivity
     * @param permissionGrantedCallback Function0<Unit>
     */
    fun FragmentActivity.requestCameraAndStorePermission(permissionGrantedCallback: () -> Unit) {
        requestPermission(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            permissionGrantedCallback
        )
    }


    /**
     * 申请 camera 和 read external storage 权限
     * @receiver Fragment
     * @param permissionGrantedCallback Function0<Unit>
     */
    fun Fragment.requestCameraAndStorePermission(permissionGrantedCallback: () -> Unit) {
        requestPermission(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            permissionGrantedCallback
        )
    }

    //endregion


    //region 权限申请主方法


    /**
     * 申请权限 for activity
     * @param activity FragmentActivity
     * @param permission Array<String> 权限列表
     * @param permissionGrantedCallback Function0<Unit> 权限被同意后执行
     */
    @SuppressLint("CheckResult")
    fun requestPermission(
        activity: FragmentActivity, permission: Array<String>,
        permissionGrantedCallback: () -> Unit
    ) {
        val reqPermissions = filterPermission(permission)
        if (reqPermissions.isEmpty()) {
            handleResult(activity, emptyList(), true, permissionGrantedCallback)
            return
        }

        XXPermissions.with(activity)
            .permission(reqPermissions)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (!all) {
                        return
                    }
                    handleResult(activity, emptyList(), all, permissionGrantedCallback)
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    handleResult(activity, permissions, false, permissionGrantedCallback)
                }
            })
    }

    /**
     * 申请权限 for fragment
     * @param fragment Fragment
     * @param permission Array<String> 权限列表
     * @param permissionGrantedCallback Function0<Unit> 权限被同意后执行
     */
    @SuppressLint("CheckResult")
    fun requestPermission(
        fragment: Fragment?, permission: Array<String>,
        permissionGrantedCallback: () -> Unit
    ) {
        fragment ?: return
        val reqPermissions = filterPermission(permission)
        if (reqPermissions.isEmpty()) {
            handleResult(fragment, emptyList(), true, permissionGrantedCallback)
            return
        }

        XXPermissions.with(fragment)
            .permission(reqPermissions)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (!all) {
                        return
                    }
                    handleResult(fragment, emptyList(), all, permissionGrantedCallback)
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    handleResult(fragment, permissions, false, permissionGrantedCallback)
                }
            })
    }


    /**
     * 过滤权限
     * 1. android10 以上不再申请 存储写入权限
     * @param permission Array<String>
     * @return Array<String>
     */
    private fun filterPermission(permission: Array<String>): Array<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return permission
                .filter { it !in listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }
                .toTypedArray()
        } else {
            return permission
        }
    }

    //endregion

    //region 结果处理和dialog显示

    /**
     * 处理申请结果
     *
     * @param obj Any?
     * @param ret List<Permission>
     * @param permissionGrantedCallback Function0<Unit>
     */
    private fun handleResult(
        obj: Any?,
        noGrantPermissions: List<String>,
        allGrant: Boolean,
        permissionGrantedCallback: () -> Unit
    ) {
        obj ?: return
        //全部同意
        if (allGrant) {
            permissionGrantedCallback()
            return
        }
        val haveNoAsk = if (obj is Activity) {
            XXPermissions.isPermanentDenied(obj, noGrantPermissions)
        } else if (obj is Fragment && obj.activity != null) {
            XXPermissions.isPermanentDenied(obj.activity, noGrantPermissions)
        } else {
            return
        }
        //未被同意的权限
        if (haveNoAsk) {
            //已被选择不再询问
            showDialog(obj, noGrantPermissions, permissionGrantedCallback, true)
        } else {
            //未被选中不再询问
            showDialog(obj, noGrantPermissions, permissionGrantedCallback, false)
        }
    }

    /**
     * 权限未被同意显示弹窗
     *
     * @param obj Any?
     * @param ret List<Permission>
     * @param permissionGrantedCallback Function0<Unit>
     * @param noAskAgain Boolean
     */
    private fun showDialog(
        obj: Any?, ret: List<String>,
        permissionGrantedCallback: () -> Unit,
        noAskAgain: Boolean
    ) {
        obj ?: return
        val context = when (obj) {
            is FragmentActivity -> obj
            is Fragment -> obj.activity ?: return
            else -> throw InvalidParameterException()
        }

        AlertDialog.Builder(context)
            .setIcon(context.packageManager.getApplicationIcon(context.packageName))
            .setTitle("权限申请失败")
            .setMessage(getPermissionDialogMessage(ret))
            .setPositiveButton(if (!noAskAgain) "再次许可授权" else "去设置") { dialog, _ ->
                dialog.dismiss()
                //不再询问确设置页面
                if (noAskAgain) {
                    XXPermissions.startPermissionActivity(context, *ret.toTypedArray())
                } else {
                    requestAgain(obj, ret, permissionGrantedCallback)
                }
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    /** 设置权限说明 */
    private fun getPermissionDialogMessage(permissions: List<String>): String {
        val info = getPermissionInfo(permissions)
        return "无法获取以下权限,请检查:\n" + info.joinToString("\n")
    }

    /**
     * 获取权限对应 的说明性文字
     * @param permission List<String> 权限列表
     * @return Set<String> 说明文字
     */
    private fun getPermissionInfo(permission: List<String>): Set<String> {
        val set = androidx.collection.ArraySet<String>()
        permission.forEach { item ->
            val msg = when (item) {
                android.Manifest.permission.CAMERA -> "访问相机权限"

                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE -> "访问存储权限"

                android.Manifest.permission.READ_CONTACTS -> "访问通讯录权限"

                android.Manifest.permission.READ_PHONE_STATE -> "访问电话信息权限"

                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION -> "访问位置信息权限"
                else -> null
            }
            set.add(msg)
        }
        return set
    }


    private fun requestAgain(
        obj: Any,
        permissions: List<String>,
        permissionGrantedCallback: () -> Unit
    ) {
        val permissionArray = permissions.toTypedArray()

        if (obj is FragmentActivity) {
            requestPermission(obj, permissionArray, permissionGrantedCallback)
            return
        }
        if (obj is Fragment) {
            requestPermission(obj, permissionArray, permissionGrantedCallback)
            return
        }
        throw IllegalArgumentException("obj must be Fragment/FragmentActivity")
    }
    //endregion

}


