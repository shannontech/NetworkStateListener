package com.mobile.shannon.networkstate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mobile.shannon.networkstate.annotations.NetworkDisconnect
import com.mobile.shannon.networkstate.annotations.NetworkStateChange
import com.mobile.shannon.networkstate.annotations.NetworkType

class NetworkStateReceiver : BroadcastReceiver() {
    private var networkType = NetworkType.NONE
    private val mObserverMap = HashMap<Any, List<MethodEntity>>()
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null || intent.action == null) {
            Log.e("NetworkStateReceiver", "广播异常了")
            return
        }
        if (intent.action!!.equals("android.net.conn.CONNECTIVITY_CHANGE", ignoreCase = true)
            || intent.action!!.equals("android.net.wifi.WIFI_STATE_CHANGED", ignoreCase = true)
            || intent.action!!.equals("android.net.wifi.STATE_CHANGE", ignoreCase = true)
        ) {
            networkType = NetworkManager.getNetworkType()
            Log.e("NetworkStateReceiver", "网络状态变化了：$networkType")
            notifyStateChange(networkType)
//            if (NetworkManager.isNetworkAvailable()) {
//                Log.i("NetworkStateReceiver", "网络连上了")
////                mNetChangeObserver.onConnected(type)
//            } else {
//                Log.i("NetworkStateReceiver", "网络断开了")
////                mNetChangeObserver.onDisConnected()
//            }
        }
    }

    private fun notifyStateChange(@NetworkType networkType: String) {
        mObserverMap.forEach { entry ->
            entry.value.forEach {
                if (it.networkType.isBlank()) {
                    it.method.invoke(entry.key, networkType)
                } else if (it.networkType == networkType) {
                    it.method.invoke(entry.key)
                }
            }
        }
    }

    fun registerObserver(obj: Any) {
        if (mObserverMap[obj] == null) {
            mObserverMap[obj] = getAnnotationMethodEntity(obj)
        }
    }

    fun unRegisterObserver(obj: Any) {
        mObserverMap.remove(obj)
    }

    private fun getAnnotationMethodEntity(obj: Any): List<MethodEntity> {
        obj.javaClass.methods.apply {
            val methodList = ArrayList<MethodEntity>()
            for (method in this) {
                method.getAnnotation(NetworkStateChange::class.java)?.apply {
                    methodList.add(
                        MethodEntity(
                            obj.javaClass,
                            this.networkType,
                            method
                        )
                    )

                }
            }
            return methodList
        }
    }
}