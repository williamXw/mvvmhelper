package com.gexiaobao.hdw.bw.data.response

/**
 * created by : huxiaowei
 * @date : 20220919
 * Describe :
 */
data class LoginRegisterReq(
    val androidid: String,
    val appInstanceId: String,
    val appVersion: String = "",
    val customerId: Int = 0,
    val customerMobile: String = "",
    val deviceId: String = "",
    val deviceType: String = "",
    val fcmToken: String = "",
    val ip: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val marketId: Int = 0,
    val noteToken: String = "",
    val otpCode: String = "",
    val regGaid: String = ""
)
