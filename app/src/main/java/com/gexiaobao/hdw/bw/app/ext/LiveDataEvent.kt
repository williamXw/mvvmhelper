package com.gexiaobao.hdw.bw.app.ext

import com.gexiaobao.hdw.bw.data.response.LoginInfoResponse
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * 作者　: hegaojian
 * 时间　: 2022/5/23
 * 描述　: 全局发送消息 通过LiveData实现
 */
object LiveDataEvent {

    //示例：登录成功发送通知
    val loginEvent = UnPeekLiveData<Boolean>()

    /** 通知认证成功*/
//    val kycSucceeded = UnPeekLiveData<Boolean>()

}