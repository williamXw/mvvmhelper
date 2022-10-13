package com.gexiaobao.hdw.bw.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by : huxiaowei
 * @date : 20221013
 * Describe :
 */
@Parcelize
data class CustomerCardInfo(
    val panNo: String = "",
    val aadHaarNo: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val pinCode: String = "",
    val address: String = "",
    val idCardNumber: String = "",
    val idCardName: String = "",
) : Parcelable
