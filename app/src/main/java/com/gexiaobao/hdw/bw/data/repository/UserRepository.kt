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
    fun logOut(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.LOGIN_OUT)).setBody(body)
            .toOkResponse()
    }

    /**提交紧急联系人*/
    fun pushUrgencyContact(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_EXTENSION_PUSH_URGENCY_CONTACT)).setBody(body)
            .toOkResponse()
    }

    /**获取首页需要展示的借贷相关信息*/
    fun fetchHomeInfo(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CORE_HOME_FETCH_HOME_INFO)).setBody(body)
            .toOkResponse()
    }

    /**获取银行列表*/
    fun fetchBanks(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_BANK_FETCH_BANKS)).setBody(body)
            .toOkResponse()
    }

    /**认证并绑定银行卡*/
    fun bindBankCard(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_BANK_CUSTOMER_BIND_BANKCARD)).setBody(body)
            .toOkResponse()
    }

    /**获取首页产品列表*/
    fun fetchProducts(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CORE_PRODUCT_FETCH_PRODUCTS)).setBody(body)
            .toOkResponse()
    }

    /**身份证背照ocr*/
    fun kycIDCardBackOrc(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_KYC_ID_CARD_BACK_OCR)).setBody(body)
            .toOkResponse()
    }

    /**身份证前照ocr*/
    fun kycIDCardFrontOrc(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_KYC_ID_CARD_FRONT_OCR)).setBody(body)
            .toOkResponse()
    }

    /**PanOcr*/
    fun kycIDCardPanOrc(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_KYC_PAN_OCR)).setBody(body)
            .toOkResponse()
    }

    /**身份证ocr界面-提交客户调整后的基础信息*/
    fun submitAdJustInfo(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_KYC_SUBMIT_ADJUST_INFO)).setBody(body)
            .toOkResponse()
    }

    /**   更新用户的firebase instance 及 fcm token   */
    fun fcmTokenUp(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CUSTOMER_FCMPUSH_CUSTOMER_FCM_TOKEN_UP)).setBody(body)
            .toOkResponse()
    }

    /**   获取协议地址   */
    fun fetchAgreement(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CORE_APP_FETCH_AGREEMENT)).setBody(body)
            .toOkResponse()
    }

    /**  判断是否要进行升级   */
    fun fetchAppVersion(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CORE_APP_FETCH_APP_VERSION_V2)).setBody(body)
            .toOkResponse()
    }

    /**  获取还款详情信息   */
    fun getRepayByBorrow(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CORE_PAY_GET_REPAY_BY_BORROWID)).setBody(body)
            .toOkResponse()
    }

    /**  获取延期还款的付款信息   */
    fun fetchRollRepayInfo(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CORE_PAY_FETCH_ROLL_REPAY_INFO)).setBody(body)
            .toOkResponse()
    }

    /**  发起线上还款   */
    fun fetchRepayLink(body: RequestBody): Await<Response> {
        return RxHttp.postBody(EncryptUtil.encode(NetUrl.CORE_PAY_FETCH_REPAY_LINK)).setBody(body)
            .toOkResponse()
    }

}

