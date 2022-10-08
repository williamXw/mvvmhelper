package com.gexiaobao.hdw.bw.data.response

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
class RegisterParams(
    val name: String,
    val password: String,
    val mobile: String,
    val gender: Int = 0,
    val role: Int = 5,
    val idCard: String,
    val code: String,
    val realName: String,
    val icon: Int = 0,
    val background: Int = 0,
    val address: String,
    val detailedAddress: String,
    val sectionOrg: Int = 0
)