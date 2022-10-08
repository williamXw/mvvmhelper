package com.gexiaobao.hdw.bw.app.ext

import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.widget.CustomToolBar


/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　:
 */

/**
 * 初始化有返回键的toolbar
 */
fun CustomToolBar.initBack(
    titleStr: String = "标题",
    backImg: Int = R.drawable.ic_back,
    onBack: (toolbar: CustomToolBar) -> Unit
): CustomToolBar {
//    this.setCenterTitle(titleStr)
//    this.getBaseToolBar().setNavigationIcon(backImg)
//    this.getBaseToolBar().setNavigationOnClickListener { onBack.invoke(this) }
    return this
}