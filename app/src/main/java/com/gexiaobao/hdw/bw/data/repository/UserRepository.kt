package com.gexiaobao.hdw.bw.data.repository

import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.data.response.*
import okhttp3.RequestBody
import okhttp3.Response
import rxhttp.toOkResponse
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/2
 * 描述　: 数据仓库
 */
object UserRepository {


    /**获取手机验证码*/
    fun customerOtp(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_OTP)).setBody(body)
            .toOkResponse()
    }


    /**登录*/
    fun login(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.LOGIN)).setBody(body)
            .toOkResponse()
    }

    /**退出登录*/
    fun logOut(): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.LOGIN_OUT)).setBody("")
            .toOkResponse()
    }

    /**提交紧急联系人*/
    fun pushUrgencyContact(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_EXTENSION_PUSH_URGENCY_CONTACT)).setBody(body)
            .toOkResponse()
    }

    /**注册*/
    fun register(body: RegisterParams): Await<LoginInfoResponse> {
        return RxHttp.postBody(NetUrl.REGISTER).setBody(body).toResponse()
    }

    /**重置密码*/
    fun reSetPwd(body: ResetPwdParams): Await<Any> {
        return RxHttp.patchBody(NetUrl.RESET_PASSWORD).setBody(body).toResponse()
    }

//    /**
//     * 获取列表信息
//     */
//    fun getList(pageIndex: Int): Await<ApiPagerResponse<Any>> {
//        return RxHttp.get(NetUrl.HOME_LIST, pageIndex)
//            .toResponse()
//    }

}

