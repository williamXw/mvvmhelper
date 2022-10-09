package com.gexiaobao.hdw.bw.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.data.repository.UserRepository
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.ext.rxHttpRequestCallBack
import me.hgj.mvvmhelper.net.LoadingType
import okhttp3.RequestBody
import okhttp3.Response

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
class MainViewModel : BaseViewModel() {

    /**退出登录*/
    fun logOutCallBack(): MutableLiveData<Response>? {
        return rxHttpRequestCallBack {
            onRequest = {
                iAwaitLiveData?.value = UserRepository.logOut().await()
            }
            loadingType = LoadingType.LOADING_DIALOG
            loadingMessage = "请求中....."
            requestCode = NetUrl.LOGIN_OUT
        }
    }
}