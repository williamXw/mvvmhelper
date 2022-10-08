package com.gexiaobao.hdw.bw.ui.fragment.account

import android.os.Bundle
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentAboutUsBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.AccountFragmentViewModel

/**
 * created by : huxiaowei
 * @date : 20221003
 * Describe :
 */
class AboutUsFragment : BaseFragment<AccountFragmentViewModel, FragmentAboutUsBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
            }
        }
    }
}