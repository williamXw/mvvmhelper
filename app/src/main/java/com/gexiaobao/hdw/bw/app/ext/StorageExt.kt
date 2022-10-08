package com.gexiaobao.hdw.bw.app.ext

import com.tencent.mmkv.MMKV
import com.gexiaobao.hdw.bw.data.annotation.ValueKey

/**
 * 作者　: hegaojian
 * 时间　: 2021/7/8
 * 描述　:
 */

/**
 * 获取MMKV
 */
val mmkv: MMKV by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    MMKV.mmkvWithID(ValueKey.MMKV_APP_KEY)
}


