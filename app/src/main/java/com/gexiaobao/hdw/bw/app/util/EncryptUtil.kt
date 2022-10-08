package com.gexiaobao.hdw.bw.app.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.*
import kotlin.experimental.xor


/**
 * Created by Win on 2022/03/29
 */
object EncryptUtil {

    //    const val key = "ecpbendakil"
    const val key = "djaskdjoiwlkjl33sd"
    const val level = 4

    private fun parseByte2HexStr(buf: ByteArray): String {
        val sb = StringBuffer()
        for (i in buf.indices) {
            var hex = Integer.toHexString((buf[i].toInt() and 0xFF))
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex.uppercase(Locale.getDefault()))
        }
        return sb.toString()
    }

    private fun parseHexStr2Byte(hexStr: String): ByteArray? {
        if (hexStr.isEmpty()) return null
        val result = ByteArray(hexStr.length / 2)
        for (i in 0 until hexStr.length / 2) {
            val high = hexStr.substring(i * 2, i * 2 + 1).toInt(16)
            val low = hexStr.substring(i * 2 + 1, i * 2 + 2).toInt(16)
            result[i] = (high * 16 + low).toByte()
        }
        return result
    }


    fun encode(res: String?): String {
        if (res.isNullOrEmpty()) {
            return ""
        }
        try {
            val bs = res.toByteArray()
            for (i in bs.indices) {
                bs[i] = (bs[i] xor key.hashCode().toByte())
            }
            return parseByte2HexStr(bs)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }


    fun decode(res: String?): String {
        if (res.isNullOrEmpty()) {
            return ""
        }
        try {
            val bs = parseHexStr2Byte(res)
            for (i in bs!!.indices) {
                bs[i] = (bs[i] xor key.hashCode().toByte())
            }
            return String(bs)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun encryptBody(bodyString: String?): String {
        if (bodyString.isNullOrEmpty()) {
            return ""
        }
        var bodyMap = hashMapOf<String, Any>()
        if (level == 1) {
            //单层即不嵌套
            return bodyString
        } else {
            for (i in 2..level) {
                val currentMap = hashMapOf<String, Any>()
                if (i == 2) {
                    putRanParams(currentMap)//第一层封装
                    currentMap[key] = bodyString//最后把加密后的真实数据put进去
                } else {
                    putRanParams(currentMap)
                    currentMap[key] = bodyMap
                }
                bodyMap = currentMap.clone() as HashMap<String, Any>
            }
            return GsonUtil.toJson(bodyMap)!!
        }
    }

    /**
     * 层级嵌套加密参
     */
//    fun encryptBody(bodyString: String?): String {
//        val secondMap = hashMapOf<String, Any>()//第二层嵌套
//        val thirdMap = hashMapOf<String, Any>()//第三层嵌套
//        val fourthMap = hashMapOf<String, Any>()//第四层嵌套
//
//        if (bodyString.isNullOrEmpty()) {
//            return ""
//        }
//        if (level == 1) {
//            //单层即不嵌套
//            return bodyString
//        } else {
//            for (i in 2..level) {
//                if (i == 2) {
//                    putRanParams(secondMap)
//                    secondMap[key] = bodyString//把加密后的真实数据put进去
//                } else if (i == 3) {
//                    val secondBody = Gson().toJson(secondMap)//将map转换成json
//                    putRanParams(thirdMap)
//                    thirdMap[key] = secondBody
//                } else {
//                    val thirdBody = Gson().toJson(thirdMap)
//                    putRanParams(fourthMap)
//                    fourthMap[key] = thirdBody
//                }
//            }
//            return GsonUtil.toJson(fourthMap)!!
//        }
//    }

    fun decodeBody(bodyString: String?): String {
        if (bodyString.isNullOrEmpty()) {
            return ""
        }
        var res = GsonUtil.fromJson(bodyString, object : TypeToken<Map<String, Any>>() {})
        var response = ""
        try {
            for (i in 1 until level) {
                val currentRes = decodeRes(res!!)
                if (i != level - 1) {
                    res = currentRes as Map<String, Any>
                } else {
                    response = currentRes as String
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    private fun decodeRes(map: Map<String, Any>): Any {
        return map[key]!!
    }

    private fun putRanParams(map: HashMap<String, Any>) {
        val randomParams = Random().nextInt(3) + 2//2-4随机数
        for (j in 1..randomParams) {
            val key = getRanString(Random().nextInt(10) + 1)//1-10随机数
            val value = getRanString(Random().nextInt(10) + 1)
            map[key] = value
        }
    }

    private fun getRanString(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuffer()
        for (i in 0 until length) {
            val number: Int = random.nextInt(str.length - 1)
            sb.append(str[number])
        }
        return sb.toString()
    }

    /**
     * 将map数据转换为 普通的 json RequestBody
     * @param map 以前的请求参数
     * @return
     */
    fun convertMapToBody(map: Map<*, *>?): RequestBody? {
        return RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(map).toString()
        )
    }

    /**
     * 将map数据转换为图片，文件类型的  RequestBody
     * @param map 以前的请求参数
     * @return 待测试
     */
    fun convertMapToMediaBody(map: Map<*, *>?): RequestBody? {
        return RequestBody.create(
            "multipart/form-data; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(map).toString()
        )
    }

    fun toRequestBody(vararg params: Pair<String, Any?>): RequestBody {
        return RequestBody.create( "application/json".toMediaTypeOrNull(), toJSONObject(*params).toString())
    }

    fun toJSONObject(vararg params: Pair<String, Any?>): JSONObject {
        val param = JSONObject()
        for (i in params) {
            val value = if (i.second == null) "" else i.second
            param.put(i.first, value)
        }
        return param
    }
}