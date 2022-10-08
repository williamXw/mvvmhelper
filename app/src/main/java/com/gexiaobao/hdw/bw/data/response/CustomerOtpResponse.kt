package com.gexiaobao.hdw.bw.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by : huxiaowei
 * @date : 20220921
 * Describe :
 */
@Parcelize
data class CustomerOtpResponse(
    val noteToken: String = "",
) : Parcelable

