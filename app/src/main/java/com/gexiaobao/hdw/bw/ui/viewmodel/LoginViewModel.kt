package com.gexiaobao.hdw.bw.ui.viewmodel

import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.data.repository.UserRepository
import com.gexiaobao.hdw.bw.data.response.LoginInfoResponse
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.core.databinding.BooleanObservableField
import me.hgj.mvvmhelper.core.databinding.StringObservableField
import me.hgj.mvvmhelper.ext.rxHttpRequestCallBack
import me.hgj.mvvmhelper.net.LoadingType
import okhttp3.RequestBody
import okhttp3.Response

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/3
 * 描述　:
 */
class LoginViewModel : BaseViewModel() {

    val mobileNum = StringObservableField()
    val deal = StringObservableField()
    var isChecked = BooleanObservableField()
    var title = StringObservableField()

    var loginResult = MutableLiveData<LoginInfoResponse>()

    /**设置手机号如果是0开头 则最多输入11位 都则最多输入10位*/
    var mobilMax = object : ObservableInt(mobileNum) {
        override fun get(): Int {
            return if (mobileNum.get().startsWith("0")) {
                11
            } else {
                10
            }
        }
    }

//    var changeBtnBg = object : ObservableInt(mobileNum, isChecked) {
//        override fun get(): Int {
//            return if (mobileNum.get().startsWith("0")) {
//                if (mobileNum.get().length == 11 && isChecked.get()) {
//                    R.drawable.round_btn_12_click
//                } else {
//                    R.drawable.round_btn_12
//                }
//            } else {
//                if (mobileNum.get().length == 10 && isChecked.get()) {
//                    R.drawable.round_btn_12_click
//                } else {
//                    R.drawable.round_btn_12
//                }
//            }
//        }
//    }


    /**获取验证码*/
    fun customerOtpCallBack(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.customerOtp(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG //选传 默认为 LoadingType.LOADING_NULL
            loadingMessage = "请求中....." // 选传
            requestCode = NetUrl.CUSTOMER_OTP // 选传，如果要判断接口错误业务的话必传
        }
    }

    /**验证码登录*/
    fun loginCallBack(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.login(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
            requestCode = NetUrl.LOGIN
        }
    }
}