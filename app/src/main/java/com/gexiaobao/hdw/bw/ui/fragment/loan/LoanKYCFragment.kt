package com.gexiaobao.hdw.bw.ui.fragment.loan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.dialog.RxDialogSure
import com.gexiaobao.hdw.bw.app.dialog.RxLoanDialogSure
import com.gexiaobao.hdw.bw.app.ext.init
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.data.response.ProductsListResponse
import com.gexiaobao.hdw.bw.databinding.FragmentKycLoanBinding
import com.gexiaobao.hdw.bw.ui.adapter.ProductAdapter
import com.gexiaobao.hdw.bw.ui.view.PeterTimeCountRefresh
import com.gexiaobao.hdw.bw.ui.viewmodel.LoanFragmentViewModel
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import me.hgj.mvvmhelper.util.decoration.SpaceItemDecoration
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json.JSONObject
import java.text.DecimalFormat

/**
 *  author : huxiaowei
 *  date : 2022/9/29 20:58
 *  description : 认证之后的首页
 */
class LoanKYCFragment : BaseFragment<LoanFragmentViewModel, FragmentKycLoanBinding>() {

    private lateinit var timeCount: PeterTimeCountRefresh
    private var peroidUnit = 0//周期单位 10：天  20：月
    private var productDataList: ArrayList<ProductsListResponse> = arrayListOf()
    private val productAdapter: ProductAdapter by lazy { ProductAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        fetchHomeInfo()

        //初始化recyclerView
        mBind.rvLoanKyc.init(LinearLayoutManager(context), productAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(10f)))
        }
        val view = LayoutInflater.from(context).inflate(R.layout.head_product_view, null)
        mBind.rvLoanKyc.addHeaderView(view)

        //初始化 SwipeRefreshLayout
        mBind.swipeRefresh.init {
            //触发刷新监听时请求数据
            fetchProducts()
        }
    }

    override fun initData() {
        super.initData()
        fetchProducts()
    }

    /**
     * 获取首页产品列表
     */
    private fun fetchProducts() {
        LogUtils.debugInfo(EncryptUtil.decode("5915190413590604191203150259101302151E2604191203150205"))
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("ip") to "",
            EncryptUtil.encode("marketId") to Constant.MARKET_ID
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.fetchProducts(paramsBody)?.observe(this) {
            mBind.swipeRefresh.isRefreshing = false
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {
                productDataList.clear()
                val dataBean = ProductsListResponse()
                val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                val productList = JSONObject(data.toString()).getJSONArray("060419120315023A1F0502")
                val a1 = EncryptUtil.decode("1819041B171A3F1802130413050224170213")
                val a2 = EncryptUtil.decode("1A191718371B19031802")
                val a3 = EncryptUtil.decode("191A1235030502191B130425190402")
                val a4 = EncryptUtil.decode("191A122604191203150225190402")
                val a5 = EncryptUtil.decode("0604191203150238171B13")
                val a6 = EncryptUtil.decode("18130126041912031502250F053703021925190402")
                val a7 = EncryptUtil.decode("1B1304151E171802220F061325190402")
                val a8 = EncryptUtil.decode("1813012604191203150225190402")
                val a9 = EncryptUtil.decode("041315131F0013371B19031802")
                val a10 = EncryptUtil.decode("18131312241306170F371B19031802")
                val a11 = EncryptUtil.decode("051304001F151324170213")
                val a12 = EncryptUtil.decode("021912170F321305023419040419013519031802")
                val a13 = EncryptUtil.decode("131817141A13")
                val a14 = EncryptUtil.decode("190013041203133F1802130413050224170213")
                val a15 = EncryptUtil.decode("021912170F3A131002381313123419040419013519031802")
                for (i in 0 until productList.length()) {
                    val result = productList[i].toString()
                    dataBean.icoUrl = JSONObject(result).getString("1F151923041A")
                    dataBean.id = JSONObject(result).getInt("1F12")
                    dataBean.productName =
                        JSONObject(result).getString("0604191203150238171B13")
                    dataBean.loanAmount =
                        JSONObject(result).getInt("1A191718371B19031802")
                    dataBean.receiveAmount =
                        JSONObject(result).getInt("041315131F0013371B19031802")
                    productDataList.addAll(
                        listOf(
                            ProductsListResponse(
                                dataBean.icoUrl,
                                dataBean.id,
                                dataBean.loanAmount,
                                0.0,
                                dataBean.productName,
                                dataBean.receiveAmount
                            )
                        )
                    )
                }
                productAdapter.setList(productDataList)
            }
        }

    }

    private fun fetchHomeInfo() {
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.fetchHomeInfo(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {
                val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                if (data != null) {
                    val bbbbb = EncryptUtil.decode("051304001F1513371B19031802")//利息
                    val bbbbbb = EncryptUtil.decode("1B1918021E0524170213")//月利率

                    peroidUnit = data.getInt("061304191F1223181F02")
                    mViewModel.amount.set(RxDataTool.getAmountValue(data.getDouble("1A191718371B19031802")))//借款金额
                    mViewModel.period.set(data.getString("061304191F12"))//借款周期
                    mViewModel.amountReceived.set("₹ " + data.getString("041315131F0013371B19031802"))//到账金额
                    mViewModel.youRepay.set("₹ " + data.getString("061304191F1223181F02"))
                    mViewModel.repaymentDate.set(
                        getRepaymentDay(
                            peroidUnit, data.getString("061304191F12").toInt()
                        )
                    )
                    mViewModel.annualInterestRate.set(data.getString("0F13170424170213") + "%")//年利率
                }
            }
        }
    }

    private fun getRepaymentDay(unit: Int, num: Int): String {
        val year = DateTime().year
        val month = DateTime().monthOfYear
        val day = DateTime().dayOfMonth
        val dateTime = DateTime(year, month, day, 0, 0, 0, 0)
        when (unit) {
            10 -> {
                return dateTime.plusDays(num).toString("yyyy-MM-dd")
            }
            20 -> {
                return dateTime.plusMonths(num).toString("yyyy-MM-dd")
            }
        }
        return ""
    }

    override fun onResume() {
        super.onResume()
        timeCount = PeterTimeCountRefresh(mBind.tvLoanKycTimeCount, 180000, 1000)
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.btnLoanNow, mBind.tvLoanOrders) {
            when (it) {
                mBind.btnLoanNow -> {
                    showLoanSureDialog()
//                    timeCountThree()
                }
                mBind.tvLoanOrders -> {

                }
            }
        }
    }

    private fun showLoanSureDialog() {
        val rxDialog = RxLoanDialogSure(context)
        rxDialog.setFullScreenWidth()
        rxDialog.btnConfirm.setOnClickListener {
            RxToast.showToast("点击了")
        }
        rxDialog.show()
    }

    private fun timeCountThree() {
        timeCount.setOnTimerFinishListener(object : PeterTimeCountRefresh.OnTimerFinishListener {
            override fun onTimerFinish() {
                RxToast.info("-------------->>>>>>>>><<<<<<<<")
            }

        })
        timeCount.setOnTimerProgressListener(object :
            PeterTimeCountRefresh.OnTimerProgressListener {
            override fun onTimerProgress(millisUntilFinished: Long) {
                val dec = DecimalFormat("##.##")
                var sec = ""
                sec = if (dec.format((millisUntilFinished % 60000) / 1000)
                        .equals(0) || dec.format((millisUntilFinished % 60000) / 1000).toInt() < 10
                ) {
                    "0" + dec.format((millisUntilFinished % 60000) / 1000)
                } else {
                    dec.format((millisUntilFinished % 60000) / 1000)
                }
                mBind.tvLoanKycTimeCount.text =
                    "0" + (millisUntilFinished / 60000).toInt() + ":" + sec
            }

        })
        timeCount.start()
    }

    override fun onPause() {
        super.onPause()
        if (timeCount != null) {
            timeCount.cancel()
        }
    }

}