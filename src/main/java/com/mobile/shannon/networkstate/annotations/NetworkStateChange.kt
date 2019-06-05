package com.mobile.shannon.networkstate.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkStateChange(@NetworkType val networkType: String = "")