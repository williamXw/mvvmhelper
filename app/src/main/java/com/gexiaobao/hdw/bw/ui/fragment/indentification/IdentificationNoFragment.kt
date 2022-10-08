package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentIndentificationNoBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.IdentificationViewModel


/**
 *  author : huxiaowei
 *  date : 2022/9/25 13:39
 *  description :
 */
class IdentificationNoFragment : BaseFragment<IdentificationViewModel, FragmentIndentificationNoBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
    }

    override fun initData() {
        super.initData()
        mViewModel.title.set("Identification")
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.btnContinue) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.btnContinue -> {
//                    nav().navigateAction(R.id.action_inden_no_to_inden_face)
                    nav().navigateAction(R.id.action_inden_no_to_inden_contacts)
                }
            }
        }
    }

    override fun onRequestSuccess() {
        super.onRequestSuccess()

    }
}