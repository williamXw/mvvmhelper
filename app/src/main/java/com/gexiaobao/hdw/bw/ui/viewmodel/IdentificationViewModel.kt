package com.gexiaobao.hdw.bw.ui.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import me.hgj.mvvmhelper.base.BaseViewModel
import me.hgj.mvvmhelper.core.databinding.StringObservableField

/**
 *  author : huxiaowei
 *  date : 2022/9/25 13:39
 *  description :
 */
class IdentificationViewModel : BaseViewModel() {

    var title = StringObservableField()
    var name = StringObservableField()
    var namePan = StringObservableField()
    var panNo = StringObservableField()
    var aadhaarNo = StringObservableField()
    var dateOfBirth = StringObservableField()
    var gender = StringObservableField()
    var pincode = StringObservableField()
    var email = StringObservableField()
    var address = StringObservableField()


    var bankName = StringObservableField()
    var matchWithAddahar = StringObservableField()
    var enterIFSCCode = StringObservableField()
    var enterAcNo = StringObservableField()
    var reEnterAcNo = StringObservableField()

    //用户名清除按钮是否显示
    var matchVisible = object : ObservableInt(enterAcNo, reEnterAcNo) {
        override fun get(): Int {
            return if (enterAcNo.get() == reEnterAcNo.get()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

}