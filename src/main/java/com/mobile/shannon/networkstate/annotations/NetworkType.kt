package com.mobile.shannon.networkstate.annotations

import android.support.annotation.StringDef


/**
 * 网络类型
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    NetworkType.UNKNOWN,
    NetworkType.WIFI,
    NetworkType.MOBILE,
    NetworkType.NONE
)
annotation class NetworkType {
    companion object {
        //wifi
        const val WIFI = "WIFI"
        //移动网络
        const val MOBILE = "MOBILE"
        //没有网络
        const val NONE = "NONE"
        //wifi与流量以外的网络
        const val UNKNOWN = "UNKNOWN"
    }
}
