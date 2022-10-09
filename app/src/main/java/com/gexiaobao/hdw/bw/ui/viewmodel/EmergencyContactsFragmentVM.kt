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
 *  author : huxiaowei
 *  date : 2022/9/27 22:55
 *  description :
 */
class EmergencyContactsFragmentVM : BaseViewModel() {

    var title = StringObservableField()
    var relation = StringObservableField()
    var mobile = StringObservableField()
    var relation2 = StringObservableField()
    var mobile2 = StringObservableField()

    /**提交紧急联系人*/
    fun pushUrgencyContactCallBack(body: RequestBody): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.pushUrgencyContact(body).await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
            requestCode = NetUrl.CUSTOMER_EXTENSION_PUSH_URGENCY_CONTACT
        }
    }

}