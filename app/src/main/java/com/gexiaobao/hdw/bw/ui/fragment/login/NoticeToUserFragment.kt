package com.gexiaobao.hdw.bw.ui.fragment.login

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.databinding.FragmentNoticeToUserBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.LoginViewModel
import com.gyf.immersionbar.ImmersionBar

/**
 * created by : huxiaowei
 * @date : 20220913
 * Describe : 用户须知
 */
class NoticeToUserFragment : BaseFragment<LoginViewModel, FragmentNoticeToUserBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).keyboardEnable(true).init()
        mBind.viewmodel = mViewModel
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
//        setOnclickNoRepeat(mBind.llForgotPwdBack, mBind.forgetCommit, mBind.forgetVerifyCodeSend) {
//            when (it.id) {
//                R.id.ll_forgot_pwd_back -> {
//                    nav().navigateUp()
//                }
//                R.id.forget_commit -> {
//                    changePwd()
//                }
//                R.id.forget_verify_code_send -> {
//                    getVerCode()
//                }
//            }
//        }
    }

}