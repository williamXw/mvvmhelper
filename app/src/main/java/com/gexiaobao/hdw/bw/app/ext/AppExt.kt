package com.gexiaobao.hdw.bw.app.ext

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.blankj.utilcode.util.ToastUtils
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.util.CacheUtil
import com.gexiaobao.hdw.bw.app.util.navigateAction
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

    }
    return null
}

/**
 * 加入qq聊天群
 */
fun Fragment.joinQQGroup(key: String): Boolean {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        ToastUtils.showShort("未安装手机QQ或安装的版本不支持")
        false
    }
}

/**
 * 拦截登录操作，如果没有登录跳转登录，登录过了贼执行你的方法
 */
fun NavController.jumpByLogin(action: (NavController) -> Unit) {
    if (CacheUtil.isLogin()) {
        action(this)
    } else {
        this.navigateAction(R.id.action_to_loginFragment)
    }
}

/**
 * 拦截登录操作，如果没有登录执行方法 actionLogin 登录过了执行 action
 */
fun NavController.jumpByLogin(
    actionLogin: (NavController) -> Unit,
    action: (NavController) -> Unit
) {
    if (CacheUtil.isLogin()) {
        action(this)
    } else {
        actionLogin(this)
    }
}


fun List<*>?.isNull(): Boolean {
    return this?.isEmpty() ?: true
}

fun List<*>?.isNotNull(): Boolean {
    return this != null && this.isNotEmpty()
}

/**
 * 根据索引获取集合的child值
 * @receiver List<T>?
 * @param position Int
 * @return T?
 */
inline fun <reified T> List<T>?.getChild(position: Int): T? {
    //如果List为null 返回null
    return if (this == null) {
        null
    } else {
        //如果position大于集合的size 返回null
        if (position + 1 > this.size) {
            null
        } else {
            //返回正常数据
            this[position]
        }
    }
}

