package com.mobile.shannon.networkstate

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.mobile.shannon.networkstate.annotations.NetworkType


object NetworkManager {

    lateinit var sApplication: Application
    private val mNetworkStateReceiver = NetworkStateReceiver()

    fun install(application: Application) {
        sApplication = application.apply {
            registerReceiver(mNetworkStateReceiver, IntentFilter().apply {
                addAction("android.net.conn.CONNECTIVITY_CHANGE")
                addAction("android.net.wifi.WIFI_STATE_CHANGED")
                addAction("android.net.wifi.STATE_CHANGE")
            })
        }
    }

    fun uninstall() {
        sApplication.unregisterReceiver(mNetworkStateReceiver)
    }

    fun registerObserver(obj: Any) {
        mNetworkStateReceiver.registerObserver(obj)
    }

    fun unRegisterObserver(obj: Any) {
        mNetworkStateReceiver.unRegisterObserver(obj)
    }

    /**
     * @return 是否有网络
     */
    fun isNetworkAvailable(): Boolean {
        val manager =
            sApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = manager.allNetworks
        if (networks != null) {
            for (network in networks) {
                if (manager.getNetworkInfo(network).state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * @return 网络类型
     */
    fun getNetworkType(): String {
        val manager = sApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo ?: return NetworkType.NONE
        val type = networkInfo.type
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NetworkType.MOBILE
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI
        }
        return NetworkType.UNKNOWN
    }

    /**
     * 打开网络设置界面
     * @param context
     * @param requestCode 请求跳转
     */
    fun openNetSetting(context: Context, requestCode: Int) {
        val intent = Intent("/")
        val cn = ComponentName("com.android.settings", "com.android.settings.WirelessSettings")
        intent.component = cn
        intent.action = "android.intent.action.VIEW"
        (context as Activity).startActivityForResult(intent, requestCode)
    }
}