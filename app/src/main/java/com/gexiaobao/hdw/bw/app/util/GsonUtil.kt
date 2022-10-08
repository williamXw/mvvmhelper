package com.gexiaobao.hdw.bw.app.util

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * author : huxiaowei
 * date : 2022/9/10 20:49
 * description :
 */
object GsonUtil {
    private val gson = Gson()

    /**
     * Object转json
     *
     * @param obj
     * @return
     */
    fun toJson(obj: Any?): String {
        return gson.toJson(obj)
    }

    fun <T> fromJson(json: String?, cls: Class<T>?): T {
        return gson.fromJson(json, cls)
    }

    /**
     * Json转List集合
     */
    fun <T> jsonToList(json: String?, clz: Class<T>?): List<T> {
        val type = object : TypeToken<List<T>?>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Json转List集合,遇到解析不了的，就使用这个
     */
    fun <T> fromJsonList(json: String?, cls: Class<T>?): List<T> {
        val mList: MutableList<T> = ArrayList()
        val array = JsonParser().parse(json).asJsonArray
        val mGson = Gson()
        for (elem in array) {
            mList.add(mGson.fromJson(elem, cls))
        }
        return mList
    }

    /**
     * Json转换成Map的List集合对象
     */
    fun <T> toListMap(json: String?, clz: Class<T>?): List<Map<String, T>> {
        val type = object : TypeToken<List<Map<String?, T>?>?>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Json转Map对象
     */
    fun <T> toMap(json: String?, clz: Class<T>?): Map<String, T> {
        val type = object : TypeToken<Map<String?, T>?>() {}.type
        return gson.fromJson(json, type)
    }

    fun fromJson(bodyString: String, typeToken: TypeToken<Map<String, Any>>): Map<String, Any>? {
        return gson.fromJson(bodyString, typeToken as Type)
    }
}