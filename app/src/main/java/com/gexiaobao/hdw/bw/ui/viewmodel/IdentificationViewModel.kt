package com.gexiaobao.hdw.bw.ui.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.data.repository.UserRepository
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.core.databinding.BooleanObservableField
import me.hgj.mvvmhelper.core.databinding.StringObservableField
import me.hgj.mvvmhelper.ext.rxHttpRequestCallBack
import me.hgj.mvvmhelper.net.LoadingType
import okhttp3.RequestBody
import okhttp3.Response

/**
 *  author : huxiaowei
 *  date : 2022/9/25 13:39
 *  description :
 */
class IdentificationViewModel : BaseViewModel() {

    var title = StringObservableField()
    var name = StringObservableField()
    var namePan = StringObservableField()
    var panNo = StringObservableField()
    var aadhaarNo = StringObservableField()
    var dateOfBirth = StringObservableField()
    var gender = StringObservableField()
    var pincode = StringObservableField()
    var email = StringObservableField()
    var address = StringObservableField()


    var bankName = StringObservableField()//银行名
    var beneficiaryName = StringObservableField()//收款人姓名
    var enterIFSCCode = StringObservableField()//
    var enterAcNo = StringObservableField()
    var reEnterAcNo = StringObservableField()
    var isMatch = BooleanObservableField()

    //用户名清除按钮是否显示
    var matchVisible = object : ObservableInt(enterAcNo, reEnterAcNo) {
        override fun get(): Int {
            return if (enterAcNo.get() == reEnterAcNo.get()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    /**获取银行列表*/
    fun fetchBanks(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.fetchBanks(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
//            loadingMessage = "请求中....."
            requestCode = NetUrl.CUSTOMER_BANK_FETCH_BANKS
        }
    }

}