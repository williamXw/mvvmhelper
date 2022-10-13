package com.gexiaobao.hdw.bw.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.gexiaobao.hdw.bw.data.repository.UserRepository
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.core.databinding.StringObservableField
import me.hgj.mvvmhelper.ext.rxHttpRequestCallBack
import me.hgj.mvvmhelper.net.LoadingType
import okhttp3.RequestBody
import okhttp3.Response

/**
 * created by : huxiaowei
 * @date : 20220922
 * Describe :
 */
class LoanFragmentViewModel : BaseViewModel() {

    val amount = StringObservableField()
    val period = StringObservableField()
    val amountReceived = StringObservableField()
    val youRepay = StringObservableField()
    val repaymentDate = StringObservableField()
    val annualInterestRate = StringObservableField()


    /**验证码登录*/
    fun fetchHomeInfo(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.fetchHomeInfo(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
        }
    }

    /**获取首页产品列表*/
    fun fetchProducts(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.fetchProducts(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
        }
    }
}