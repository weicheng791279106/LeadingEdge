package com.wc.cleanmvvm

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.ResponseBody
import retrofit2.http.GET
import java.lang.Exception
import java.lang.RuntimeException

/**
 * ProjectName:  LeadingEdge
 * Desc:网络接口
 * Author: weicheng
 * Date:  2022/11/1
 */

data class Response<T>(val code:Int = SUCCEED,val msg:String = "",val data:T){}





