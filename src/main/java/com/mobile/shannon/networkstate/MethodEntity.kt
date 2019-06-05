package com.mobile.shannon.networkstate

import com.mobile.shannon.networkstate.annotations.NetworkType
import java.lang.reflect.Method

data class MethodEntity(
    val targetClazz: Class<*>,
    @NetworkType val networkType: String,
    val method: Method
)