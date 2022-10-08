package com.gexiaobao.hdw.bw.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
@Parcelize
data class LoginInfoResponse(
    var blackCustomer: Boolean,//黑名单
    var customerBankCards: CustomerBankCards,//
    var customerMobile: String = "",//客户手机
    var customerName: String = "",//客户全名
    var customerSex: Int,//客户性别（0：未知；10：男；20：女；30：变性人）
    var customerType: Int,//客户类型：10：工人，20：学生，0：其他
    var customerUid: String = "",//客户唯一编号
    var g: Boolean,//
    var googleTester: Boolean,//与isG字段一样的值：是否谷歌审核人账号,true：是，false: 否
    var id: Int,//客户编号
    var marketId: Int,//贷超编号
    var marketType: Int,//贷超类型
    var old: Boolean,//是否老客
    var token: String = ""//登录token
) : Parcelable {
    enum class CustomerBankCards(
        val bankAccount: String = "",//银行账号
        val bankName: String = "",//银行名
        val bankUserName: String = "",//开户人姓名
        val customerId: Int,//客户编号//
        val id: Int,//
        val ifsc: String = "",//
        val mandatoryPassed: Boolean,//是否跳过验证，强制通过
        val mobile: String = "",//留的手机号
        val remark: String = "",//绑卡说明
        val validateResUserName: String = "",//三方校验后返回的姓名
        val validateStatus: Int,//状态：0：待验证，10：通过，20：未通过

    )
}