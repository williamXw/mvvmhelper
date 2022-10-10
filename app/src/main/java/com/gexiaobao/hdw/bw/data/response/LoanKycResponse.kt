package com.gexiaobao.hdw.bw.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
@Parcelize
class LoanKycResponse : Parcelable {

    var loanAmount: Long = 0//借款金额
    var monthsRate: Double = 0.0//月利率
    var needRepayAmount: Long = 0//应还金额
    var peroid: Int = 0//借款周期
    var peroidUnit: String = ""//周期单位 10：天  20：月
    var receiveAmount: Long = 0//到账金额
    var serviceAmount: Double = 0.0//利息
    var yearRate: Double = 0.0//年利率

}