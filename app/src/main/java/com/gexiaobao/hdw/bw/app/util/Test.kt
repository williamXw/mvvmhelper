package com.gexiaobao.hdw.bw.app.util

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream

/**
 * created by : huxiaowei
 *
 * @date : 20221013
 * Describe :
 */
object Test {
    /**
     * 字符串的压缩
     *
     * @param str
     * 待压缩的字符串
     * @return 返回压缩后的字符串
     * @throws IOException
     */
    @Throws(IOException::class)
    fun compress(str: String?): String? {
        if (null == str || str.isEmpty()) {
            return str
        }
        // 创建一个新的输出流
        val out = ByteArrayOutputStream()
        // 使用默认缓冲区大小创建新的输出流
        val gzip = GZIPOutputStream(out)
        // 将字节写入此输出流
        gzip.write(str.toByteArray(charset("utf-8"))) // 因为后台默认字符集有可能是GBK字符集，所以此处需指定一个字符集
        gzip.close()
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("ISO-8859-1")
    }
}