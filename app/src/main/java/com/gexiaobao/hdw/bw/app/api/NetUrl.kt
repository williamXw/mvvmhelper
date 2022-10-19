package com.gexiaobao.hdw.bw.app.api

import rxhttp.wrapper.annotation.DefaultDomain

/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　:
 */
object NetUrl {

    // 服务器请求成功的 Code值
    const val SUCCESS_CODE = 0

    // 登录过期的 Code值
    const val LOGIN_OUT_CODE = 1000

    @DefaultDomain //设置为默认域名
    const val DEV_URL = "http://115.238.46.58:20222"

    /**登录*/
    const val LOGIN = "/customer/loginOrRegByOtp"

    /**退出登录*/
    const val LOGIN_OUT = "/customer/logout"

    /**获取手机验证码接口*/
    const val CUSTOMER_OTP = "/customer/otp"

    /**提交紧急联系人*/
    const val CUSTOMER_EXTENSION_PUSH_URGENCY_CONTACT = "/customer/extension/pushUrgencyContact"

    /**获取首页需要展示的借贷相关信息*/
    const val CORE_HOME_FETCH_HOME_INFO = "/core/home/fetchHomeInfo"

    /**获取银行列表*/
    const val CUSTOMER_BANK_FETCH_BANKS = "/customer/bank/fetchBanks"

    /**认证并绑定银行卡*/
    const val CUSTOMER_BANK_CUSTOMER_BIND_BANKCARD = "/customer/bank/customerBindBankCard"

    /**获取首页产品列表*/
    const val CORE_PRODUCT_FETCH_PRODUCTS = "/core/product/fetchProducts"

    /**身份证背照ocr*/
    const val CUSTOMER_KYC_ID_CARD_BACK_OCR = "/customer/kyc/idCardBackOcr"

    /**身份证前照ocr*/
    const val CUSTOMER_KYC_ID_CARD_FRONT_OCR = "/customer/kyc/idCardFrontOcr"

    /**panocr*/
    const val CUSTOMER_KYC_PAN_OCR = "/customer/kyc/panOcr"

    /**身份证ocr界面-提交客户调整后的基础信息*/
    const val CUSTOMER_KYC_SUBMIT_ADJUST_INFO = "/customer/kyc/submitAdjustInfo"

    /**  更新用户的firebase instance 及 fcm token   */
    const val CUSTOMER_FCMPUSH_CUSTOMER_FCM_TOKEN_UP = "/customer/fcmPush/customerFcmTokenUp"

    /**  获取协议地址   */
    const val CORE_APP_FETCH_AGREEMENT = "/core/app/fetchAgreement"

    /**  判断是否要进行升级   */
    const val CORE_APP_FETCH_APP_VERSION_V2 = "/core/app/fetchAppVersionV2"

    /**  获取还款详情信息   */
    const val CORE_PAY_GET_REPAY_BY_BORROWID = "/core/pay/getRepayByBorrowId"

    /**   获取延期还款的付款信息   */
    const val CORE_PAY_FETCH_ROLL_REPAY_INFO = "/core/pay/fetchRollRePayInfo"

    /**   发起线上还款   */
    const val CORE_PAY_FETCH_REPAY_LINK = "/core/pay/fetchRepayLink"

}