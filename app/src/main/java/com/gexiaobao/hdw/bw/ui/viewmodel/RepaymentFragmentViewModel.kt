package com.gexiaobao.hdw.bw.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.data.repository.UserRepository
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.core.databinding.StringObservableField
import me.hgj.mvvmhelper.ext.rxHttpRequestCallBack
import me.hgj.mvvmhelper.net.LoadingType
import okhttp3.RequestBody
import okhttp3.Response

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
class RepaymentFragmentViewModel : BaseViewModel() {

    val repayAmount = StringObservableField()
    val repaidDate = StringObservableField()

    val totalRepayment = StringObservableField()
    val loanAmount = StringObservableField()
    val expirationTime = StringObservableField()
    val extendPaymentPeriod = StringObservableField()
    val dueTimeAfterExtension = StringObservableField()
    val overdueAmount = StringObservableField()
    val extendRepaymentFee = StringObservableField()

    /**   获取还款详情信息   */
    fun getRepayByBorrow(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.getRepayByBorrow(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
            requestCode = NetUrl.CORE_PAY_GET_REPAY_BY_BORROWID
        }
    }

    /**   获取延期还款的付款信息   */
    fun fetchRollRepayInfo(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.fetchRollRepayInfo(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
            requestCode = NetUrl.CORE_PAY_FETCH_ROLL_REPAY_INFO
        }
    }

    /**   发起线上还款   */
    fun fetchRepayLink(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.fetchRepayLink(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
            requestCode = NetUrl.CORE_PAY_FETCH_REPAY_LINK
        }
    }

}