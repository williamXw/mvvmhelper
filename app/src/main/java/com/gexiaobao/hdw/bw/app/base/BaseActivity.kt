package com.gexiaobao.hdw.bw.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.widget.CustomToolBar
import com.gyf.immersionbar.ImmersionBar
import com.jaeger.library.StatusBarUtil
import me.hgj.mvvmhelper.base.BaseVBActivity
import me.hgj.mvvmhelper.base.BaseViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　: 使用了 ViewBinding的基类 需要自定义修改什么就重写什么 具体方法可以 搜索 BaseIView 查看
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVBActivity<VM, VB>() {

    lateinit var mToolbar: CustomToolBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent()
    }

    /**
     * 设置沉浸式状态栏
     */
    private fun setStatusBarTranslucent() {
        StatusBarUtil.setTranslucentForImageViewInFragment(
            this,
            0, null
        )
        StatusBarUtil.setLightMode(this)
    }

//    override fun getTitleBarView(): View? {
//        val titleBarView = LayoutInflater.from(this).inflate(R.layout.layout_titlebar_view, null)
//        mToolbar = titleBarView.findViewById(R.id.customToolBar)
//        return titleBarView
//    }
//
//    override fun initImmersionBar() {
//        //设置共同沉浸式样式
//        if (showToolBar()) {
//            mToolbar.setBackgroundResource(R.color.colorPrimary)
//            ImmersionBar.with(this).titleBar(mToolbar).init()
//        }
//    }


}