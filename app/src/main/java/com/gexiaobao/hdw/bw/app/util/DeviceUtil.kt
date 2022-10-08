package com.gexiaobao.hdw.bw.app.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import java.util.*

/**
 * created by :
 * @date : 20220915
 * Describe :
 */
object DeviceUtil {
    /**
     * 获取app的名称
     * @param context
     * @return
     */
    fun getAppName(context: Context): String {
        var appName = ""
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            val labelRes = packageInfo.applicationInfo.labelRes
            appName = context.resources.getString(labelRes)
        } catch (e: Throwable) {
            e.printStackTrace();
        }
        return appName;
    }

    //获取软件版本号，对应AndroidManifest.xml下android:versionCode
    fun getVersionCode(activity: Activity): Int {
        var versionCodes = 0
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCodes =
                activity.packageManager.getPackageInfo(activity.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCodes
    }

    /**
     * 获取版本号名称
     *
     * @param
     * @return
     */
    fun getVerName(activity: Activity): String {
        var verName = ""
        try {
            verName =
                activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }

    /**
     * Return the version name of device's system.
     *
     * @return the version name of device's system
     */
    val sDKVersionName: String
        get() = Build.VERSION.RELEASE

    /**
     * Return version code of device's system.
     *
     * @return version code of device's system
     */
    val sDKVersionCode: Int
        get() = Build.VERSION.SDK_INT

    /**
     * Return the android id of device.
     *
     * @return the android id of device
     */
    fun getAndroidId(context: Context): String {
        val id = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return if ("9774d56d682e549c" == id) "" else id ?: ""
    }

    /**
     * Return the manufacturer of the product/hardware.
     *
     * e.g. Xiaomi
     *
     * @return the manufacturer of the product/hardware
     */
    val manufacturer: String
        get() = Build.MANUFACTURER

    /**
     * Return the model of device.
     *
     * e.g. MI2SC
     *
     * @return the model of device
     */
    val model: String
        get() {
            var model = Build.MODEL
            model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
            return model
        }

    /**
     * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
     * element in the list.
     *
     * @return an ordered list of ABIs supported by this device
     */
    val aBIs: Array<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
            } else arrayOf(Build.CPU_ABI)
        }
    private val KEY_UDID = "KEY_UDID"

    private val udid: String? = null
    /**
     * Return the unique device id.
     * <pre>{1}{UUID(macAddress)}</pre>
     * <pre>{2}{UUID(androidId )}</pre>
     * <pre>{9}{UUID(random    )}</pre>
     *
     * @return the unique device id
     */
    //    public static String getUniqueDeviceId() {
    //        return getUniqueDeviceId("", true);
    //    }
    /**
     * Return the unique device id.
     * <pre>android 10 deprecated {prefix}{1}{UUID(macAddress)}</pre>
     * <pre>{prefix}{2}{UUID(androidId )}</pre>
     * <pre>{prefix}{9}{UUID(random    )}</pre>
     *
     * @param prefix The prefix of the unique device id.
     * @return the unique device id
     */
    //    public static String getUniqueDeviceId(String prefix) {
    //        return getUniqueDeviceId(prefix, true);
    //    }
    /**
     * Return the unique device id.
     * <pre>{1}{UUID(macAddress)}</pre>
     * <pre>{2}{UUID(androidId )}</pre>
     * <pre>{9}{UUID(random    )}</pre>
     *
     * @param useCache True to use cache, false otherwise.
     * @return the unique device id
     */
    //    public static String getUniqueDeviceId(boolean useCache) {
    //        return getUniqueDeviceId("", useCache);
    //    }
    /**
     * Return the unique device id.
     * <pre>android 10 deprecated {prefix}{1}{UUID(macAddress)}</pre>
     * <pre>{prefix}{2}{UUID(androidId )}</pre>
     * <pre>{prefix}{9}{UUID(random    )}</pre>
     *
     * @param prefix The prefix of the unique device id.
     * //     * @param useCache True to use cache, false otherwise.
     * @return the unique device id
     */
    //    public static String getUniqueDeviceId(String prefix, boolean useCache) {
    //        if (!useCache) {
    //            return getUniqueDeviceIdReal(prefix);
    //        }
    //        if (udid == null) {
    //            synchronized (DeviceUtil.class) {
    //                if (udid == null) {
    //                    UtilsBridge utilsBridge = new UtilsBridge(activity);
    //                    final String id = utilsBridge.getSpUtils4Utils().getString(KEY_UDID, null);
    //                    if (id != null) {
    //                        udid = id;
    //                        return udid;
    //                    }
    //                    return getUniqueDeviceIdReal(prefix);
    //                }
    //            }
    //        }
    //        return udid;
    //    }
    //    private static String getUniqueDeviceIdReal(String prefix) {
    //        try {
    //            final String androidId = getAndroidID();
    //            if (!TextUtils.isEmpty(androidId)) {
    //                return saveUdid(prefix + 2, androidId);
    //            }
    //
    //        } catch (Exception ignore) {/**/}
    //        return saveUdid(prefix + 9, "");
    //    }
    //    private static String saveUdid(String prefix, String id) {
    //        udid = getUdid(prefix, id);
    //        UtilsBridge utilsBridge = new UtilsBridge(activity);
    //        utilsBridge.getSpUtils4Utils().put(KEY_UDID, udid);
    //        return udid;
    //    }
    private fun getUdid(prefix: String, id: String): String {
        return if (id == "") {
            prefix + UUID.randomUUID().toString().replace("-", "")
        } else prefix + UUID.nameUUIDFromBytes(id.toByteArray()).toString()
            .replace("-", "")
    }
}