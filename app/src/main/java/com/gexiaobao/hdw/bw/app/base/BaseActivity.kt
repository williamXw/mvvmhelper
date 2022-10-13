package com.gexiaobao.hdw.bw.app.base

import android.os.Bundle
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.ext.jumpByLogin
import com.gexiaobao.hdw.bw.app.util.DeviceUtil
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.KvUtils
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.widget.CustomToolBar
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.jaeger.library.StatusBarUtil
import me.hgj.mvvmhelper.base.BaseVBActivity
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.ext.showDialogMessage
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.Response
import org.json.JSONObject
import rxhttp.wrapper.exception.ParseException

/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　: 使用了 ViewBinding的基类 需要自定义修改什么就重写什么 具体方法可以 搜索 BaseIView 查看
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVBActivity<VM, VB>() {

    lateinit var mToolbar: CustomToolBar
    var androidId = ""
    var appVersion = ""
    var customerID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent()

        androidId = this?.let { it1 -> DeviceUtil.getAndroidId(it1) }.toString()
        appVersion = this?.let { it1 -> DeviceUtil.getVersionCode(it1) }.toString()
        customerID = KvUtils.decodeInt(Constant.CUSTOMER_ID)!!
    }

    /**
     * 设置沉浸式状态栏
     */
    private fun setStatusBarTranslucent() {
        StatusBarUtil.setTranslucentForImageViewInFragment(
            this,
            0, null
        )
        StatusBarUtil.setLightMode(this)
    }

    fun parseData(it: Response?): String {
        if (it!!.code == 200) {
            val dataBody = JSONObject(it.body!!.string())
            val firstKey = JSONObject(dataBody.toString()).get(RxConstants.KEY).toString()
            val secondKey = JSONObject(firstKey).get(RxConstants.KEY).toString()
            var thirdKey = JSONObject(secondKey).get(RxConstants.KEY).toString()
            val mResponse = EncryptUtil.decode(thirdKey)
            Log.i("-------------->>>", mResponse)
            val msg = JSONObject(mResponse).getString(RxConstants.MSG)
            val code = JSONObject(mResponse).getString(RxConstants.CODE).toInt()
            if (code == NetUrl.SUCCESS_CODE) {
                return mResponse
            } else {
                showDialogMessage(msg)
                if (code != NetUrl.SUCCESS_CODE) {
                    throw ParseException(code.toString(), msg, it)
                }
            }
        } else {
            if (it != null) {
                LogUtils.debugInfo(it.code.toString() + it.message)
                throw ParseException(it.code.toString(), it.message, it)
            }
        }
        return ""
    }

//    override fun getTitleBarView(): View? {
//        val titleBarView = LayoutInflater.from(this).inflate(R.layout.layout_titlebar_view, null)
//        mToolbar = titleBarView.findViewById(R.id.customToolBar)
//        return titleBarView
//    }
//
//    override fun initImmersionBar() {
//        //设置共同沉浸式样式
//        if (showToolBar()) {
//            mToolbar.setBackgroundResource(R.color.colorPrimary)
//            ImmersionBar.with(this).titleBar(mToolbar).init()
//        }
//    }


}