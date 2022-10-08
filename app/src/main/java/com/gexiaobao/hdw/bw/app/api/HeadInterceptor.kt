package com.gexiaobao.hdw.bw.app.api

import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.KvUtils
import com.gexiaobao.hdw.bw.data.commom.Constant
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　:
 */
class HeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = KvUtils.decodeString(Constant.TOKEN)
        val deviceID = KvUtils.decodeString(Constant.DEVICE_ID)
        val builder = chain.request().newBuilder()
        builder.addHeader(
            "Cookie",
            "650D00040100171A0415151A0410110D1A110A0E000B=${EncryptUtil.encode(token)}"
        )
        builder.addHeader("Cookie", "2120332C26200C21=${EncryptUtil.encode(deviceID)}")
        builder.addHeader("djaskdjoiwlkjl33sd", "4")
        return chain.proceed(builder.build())
    }

}