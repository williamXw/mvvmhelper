package com.gexiaobao.hdw.bw.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.databinding.FragmentMainBinding
import com.gexiaobao.hdw.bw.ui.fragment.loan.LoanFragment
import com.gexiaobao.hdw.bw.ui.fragment.account.AccountFragment
import com.gexiaobao.hdw.bw.ui.fragment.loan.LoanKYCFragment
import com.gexiaobao.hdw.bw.ui.fragment.repayment.RePaymentFragment
import com.gexiaobao.hdw.bw.ui.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    private val loanFragmentPage = LoanFragment()
    private val rePaymentFragmentPage = RePaymentFragment()
    private val accountFragmentPage = AccountFragment()
    private val loanKYCFragment = LoanKYCFragment()
    private var isKYC: Boolean = true

    override fun initView(savedInstanceState: Bundle?) {
        mBind.mainViewpager.addOnPageChangeListener(viewPagerListener)
        val fragmentManager = activity?.supportFragmentManager
        val fragmentPagerAdapter = object : FragmentPagerAdapter(fragmentManager!!) {
            override fun getCount(): Int {
                return 3
            }

            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 -> {
                        return if (isKYC) loanKYCFragment else loanFragmentPage
                    }
                    1 -> {
                        return rePaymentFragmentPage
                    }
                    2 -> {
                        return accountFragmentPage
                    }
                }
                return loanFragmentPage
            }

        }
        mBind.mainViewpager?.adapter = fragmentPagerAdapter
        mBind.mainBottom.setOnNavigationItemSelectedListener(navigationViewListener)
    }

    private val viewPagerListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            mBind.mainBottom.menu.getItem(position).isChecked = true
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    private val navigationViewListener = BottomNavigationView.OnNavigationItemSelectedListener {
        mBind.mainViewpager?.currentItem = it.order
        return@OnNavigationItemSelectedListener true
    }
}