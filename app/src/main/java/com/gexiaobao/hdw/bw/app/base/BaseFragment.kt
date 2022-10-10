package com.gexiaobao.hdw.bw.app.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.ext.hideSoftKeyboard
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.app.widget.CustomToolBar
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Job
import me.hgj.mvvmhelper.base.BaseDbFragment
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.ext.showDialogMessage
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.Response
import org.json.JSONObject
import rxhttp.wrapper.exception.ParseException

/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　: 使用DataBinding 需要自定义修改什么就重写什么 具体方法可以 搜索 BaseIView 查看
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseDbFragment<VM, DB>() {

    val androidId = context?.let { it1 -> DeviceUtil.getAndroidId(it1) }
    val appVersion = activity?.let { it1 -> DeviceUtil.getVersionCode(it1) }
    val customerID = KvUtils.decodeString(Constant.CUSTOMER_ID)

    var job: Job? = null

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * Fragment执行onViewCreated后触发
     */
    override fun initData() {

    }

    fun parseDataNoResult(it: Response?): Boolean {
        var code = -1
        var msg = ""
        var mResponse = ""
        if (it!!.code == 200) {
            val dataBody = JSONObject(it.body!!.string())
            val firstKey = JSONObject(dataBody.toString()).get(RxConstants.KEY).toString()
            val secondKey = JSONObject(firstKey).get(RxConstants.KEY).toString()
            var thirdKey = JSONObject(secondKey).get(RxConstants.KEY).toString()
            mResponse = EncryptUtil.decode(thirdKey)
            Log.i("-------------->>>", mResponse)
            msg = JSONObject(mResponse).getString(RxConstants.MSG)
            code = JSONObject(mResponse).getString(RxConstants.CODE).toInt()
        } else {
            if (it != null) {
                LogUtils.debugInfo(it.code.toString() + it.message)
            }
        }
        if (code == NetUrl.SUCCESS_CODE) {
            return true
        } else {
            showDialogMessage(msg)
            if (code != NetUrl.SUCCESS_CODE) {
                throw ParseException(code.toString(), msg, it)
            }
        }
        return false
    }

    fun parseData2(it: Response?): String {
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

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (job != null) {
            job?.cancel()
            job = null
        }
    }

}