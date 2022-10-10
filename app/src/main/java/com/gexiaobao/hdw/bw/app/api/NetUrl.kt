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

    //注册
    const val REGISTER = "user/register"

    //重置密码
    const val RESET_PASSWORD = "user/changePwd"


}