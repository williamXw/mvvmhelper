package com.gexiaobao.hdw.bw.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
@Parcelize
class ProductsListResponse(
    var icoUrl: String = "",
    //图标url
    var id: Int = 0,//产品编号
    var loanAmount: Int = 0,//可贷金额（可贷金额选项）
    var needRepayAmount: Double = 0.0,//应还金额(1A191718371B19031802)
    var productName: String = "",//产品名称
    var receiveAmount: Int = 0//实际收到金额
) : Parcelable