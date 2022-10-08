package com.gexiaobao.hdw.bw.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.ext.hideSoftKeyboard
import com.gexiaobao.hdw.bw.app.widget.CustomToolBar
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Job
import me.hgj.mvvmhelper.base.BaseDbFragment
import me.hgj.mvvmhelper.base.BaseViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　: 使用DataBinding 需要自定义修改什么就重写什么 具体方法可以 搜索 BaseIView 查看
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseDbFragment<VM, DB>() {

    //需要自定义修改什么就重写什么 具体方法可以 搜索 BaseIView 查看

    var job: Job? = null
//    lateinit var mToolbar: CustomToolBar

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * Fragment执行onViewCreated后触发
     */
    override fun initData() {

    }

//    override fun getTitleBarView(): View? {
//        val titleBarView = LayoutInflater.from(context).inflate(R.layout.layout_titlebar_view, null)
//        mToolbar = titleBarView.findViewById(R.id.customToolBar)
//        mToolbar.setBackgroundResource(R.color.colorTran)
//        return titleBarView
//    }

//    override fun initImmersionBar() {
//        //设置共同沉浸式样式
//        if (showToolBar()) {
//            mToolbar.setBackgroundResource(R.color.colorPrimary)
//            ImmersionBar.with(this).titleBar(mToolbar).init()
//        }
//    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (job != null) {
            job?.cancel()
            job = null
        }
    }

}