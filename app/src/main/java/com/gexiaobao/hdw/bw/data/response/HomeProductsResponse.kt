package com.gexiaobao.hdw.bw.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
@Parcelize
class HomeProductsResponse : Parcelable {

    var defaultSelectProductCount: Int = 0//默认选中的产品数
    var fkAccount: Boolean = false//是否特殊账户（true是假账户false是真账户）
    var loanRanges: List<LoanRanges> = ArrayList<LoanRanges>()//借款周期
    var productList: List<ProductList> = ArrayList<ProductList>()//借款周期
    var maxLoanProductCount: Int = 0//客户当前可贷产品数
    var repayDate: String = ""//应还日期

    enum class LoanRanges(
        var enable: Boolean = false,//是否可选择
        var loanRange: Int = 0,//周期（天）
    )

    enum class ProductList(
        var icoUrl: String = "",//图标url
        var id: Int = 0,//产品编号
        var loanAmount: Int = 0,//可贷金额（可贷金额选项）
        var needRepayAmount: Double = 0.0,//应还金额(1A191718371B19031802)
        var productName: String = "",//产品名称
        var receiveAmount: Int = 0,//实际收到金额
//        var normalInterestRate: Double = 0.0,//0604191203150238171B13
//        var oldCustomerSort: Int = 0,//18130126041912031502250F053703021925190402
//        var oldProductSort: Int = 0,//1B1304151E171802220F061325190402
//        var newProductSysAutoSort: Int = 0,//1813012604191203150225190402
//        var merchantTypeSort: Int = 0,//041315131F0013371B19031802
//        var newProductSort: Int = 0,//18131312241306170F371B19031802
//        var serviceRate: Double = 0.0,//051304001F151324170213
//        var todayDestBorrowCount: Int = 0,//021912170F321305023419040419013519031802
//        var enable: Boolean = false,//131817141A13
//        var overdueInterestRate: Double = 0.0,//190013041203133F1802130413050224170213
//        var todayLeftNeedBorrowCount: Int = 0//021912170F3A131002381313123419040419013519031802
    )
}