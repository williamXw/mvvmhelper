package com.gexiaobao.hdw.bw.app.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import androidx.annotation.RequiresApi
import java.io.*
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * 作者　: hegaojian
 * 时间　: 2021/11/2
 * 描述　:
 */
object UriUtils {

    /**
     * 将字符串压缩后Base64
     * @param primStr 待加压加密函数
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun zipString(primStr: String?): String? {
        if (primStr == null || primStr.isEmpty()) {
            return primStr
        }
        var out: ByteArrayOutputStream? = null
        var zout: ZipOutputStream? = null
        return try {
            out = ByteArrayOutputStream()
            zout = ZipOutputStream(out)
            zout.putNextEntry(ZipEntry("0"))
            zout.write(primStr.toByteArray(charset("gbk")))
            zout.closeEntry()
            val encoder = Base64.getEncoder()
            return encoder.encodeToString(out.toByteArray())
        } catch (e: IOException) {
            null
        } finally {
            if (zout != null) {
                try {
                    zout.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

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

    /**
     * 字符串的解压
     *
     * @param str
     * 对字符串解压
     * @return 返回解压缩后的字符串
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unCompress(str: String?): String? {
        if (null == str || str.isEmpty()) {
            return str
        }
        // 创建一个新的输出流
        val out = ByteArrayOutputStream()
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        val `in` = ByteArrayInputStream(str.toByteArray(charset("ISO-8859-1")))
        // 使用默认缓冲区大小创建新的输入流
        val gzip = GZIPInputStream(`in`)
        val buffer = ByteArray(256)
        var n = 0

        // 将未压缩数据读入字节数组
        while (gzip.read(buffer).also { n = it } >= 0) {
            out.write(buffer, 0, n)
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("utf-8")
    }

    /**
     * 将图片转成byte数组
     *
     * @param bitmap 图片
     * @return 图片的字节数组
     */
    fun bitmap2Byte(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        //把bitmap100%高质量压缩 到 output对象里
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * 将图片转成byte数组
     *
     * @param imageByte 图片
     * @return Base64 String
     */
    fun byte2Base64(imageByte: ByteArray?): String? {
        return if (null == imageByte) null else android.util.Base64.encodeToString(imageByte, android.util.Base64.DEFAULT)
    }

    fun getFilePathFromURI(context: Context, contentUri: Uri?): String? {
        val rootDataDir = context.getExternalFilesDir(null)
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(rootDataDir.toString() + File.separator + fileName)
            copyFile(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    fun copyFile(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class, IOException::class)
    fun copyStream(input: InputStream?, output: OutputStream?): Int {
        val BUFFER_SIZE = 1024 * 2
        val buffer = ByteArray(BUFFER_SIZE)
        val `in` = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
            }
            try {
                `in`.close()
            } catch (e: IOException) {
            }
        }
        return count
    }

    /**
     * 通过uri  获取文件路径
     *
     * @param context
     * @param imageUri
     * @return
     */
    fun getFileAbsolutePath(context: Context?, imageUri: Uri?): String? {
        if (context == null || imageUri == null) {
            return null
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getRealFilePath(context, imageUri)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && DocumentsContract.isDocumentUri(
                context,
                imageUri
            )
        ) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return uriToFileApiQ(context, imageUri)
        } else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(imageUri)) {
                imageUri.lastPathSegment
            } else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }
        return null
    }

    //此方法 只能用于4.4以下的版本
    private fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) {
            return null
        }
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)

//            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Android 10 以上适配 另一种写法
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("Range")
    private fun getFileFromContentUri(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val filePath: String
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            uri, filePathColumn, null,
            null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            try {
                filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
                return filePath
            } catch (e: Exception) {
            } finally {
                cursor.close()
            }
        }
        return ""
    }

    /**
     * Android 10 以上适配
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun uriToFileApiQ(context: Context, uri: Uri): String {
        var file: File? = null
        //android10以上转换
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            file = File(uri.path)
        } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件复制到沙盒目录
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                try {
                    val `is` = contentResolver.openInputStream(uri)
                    val cache = File(context.externalCacheDir!!.absolutePath, Math.round((Math.random() + 1) * 1000).toString() + displayName)
                    val fos = FileOutputStream(cache)
                    FileUtils.copy(`is`!!, fos)
                    file = cache
                    fos.close()
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return file!!.absolutePath
    }
}