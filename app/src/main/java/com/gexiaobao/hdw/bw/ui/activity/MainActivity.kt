package com.gexiaobao.hdw.bw.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.base.BaseActivity
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.util.CacheUtil
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.KvUtils
import com.gexiaobao.hdw.bw.app.util.RxToast
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.ActivityMainBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.MainViewModel
import me.hgj.mvvmhelper.ext.showDialogMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import rxhttp.wrapper.exception.ParseException


/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    var exitTime = 0L

    override fun initView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.main_fragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        RxToast.showToast("Press again to exit the program")
                        exitTime = System.currentTimeMillis()
                    } else {
                        val map = mapOf<String, String>()
                        var parmas = EncryptUtil.encode(JSONObject(map).toString())
                        val paramsBody = RequestBody.create(
                            "application/json; charset=utf-8".toMediaTypeOrNull(),
                            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
                        )

                        mViewModel.logOutCallBack(paramsBody)?.observe(this@MainActivity) {
                            if (it.code == 200) {
                                var code = -1
                                var msg = ""
                                var mResponse = ""
                                val dataBody = JSONObject(it.body!!.string())
                                val firstKey = JSONObject(dataBody.toString()).get(RxConstants.KEY).toString()
                                val secondKey = JSONObject(firstKey).get(RxConstants.KEY).toString()
                                var thirdKey = JSONObject(secondKey).get(RxConstants.KEY).toString()
                                mResponse = EncryptUtil.decode(thirdKey)
                                Log.i("-------------->>>", mResponse)
                                msg = JSONObject(mResponse).getString(RxConstants.MSG)
                                code = JSONObject(mResponse).getString(RxConstants.CODE).toInt()
                                if (code == NetUrl.SUCCESS_CODE) {
                                    KvUtils.encode(Constant.TOKEN, "")
                                    LiveDataEvent.loginEvent.value = false
                                    KvUtils.encode(Constant.ISLOGIN, false)
                                    CacheUtil.setIsLogin(false)
                                    finish()
                                } else {
                                    showDialogMessage(msg)
                                    if (code != NetUrl.SUCCESS_CODE) {
                                        throw ParseException(code.toString(), msg, it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}