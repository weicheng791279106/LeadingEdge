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

data class UserInfo(val userName: String = ""){}

val api by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
    RetrofitClient.retrofitClient.create(Api::class.java)
}

interface Api {

    @GET("getUserData")
    suspend fun getUserData():Response<UserInfo>

    @GET("getUsers")
    suspend fun getUsers():Response<List<UserInfo>>

}

/**
 * 假数据拦截器
 * */
class MockInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        if (chain.request().url().toString().endsWith("getUserData")) {
            val json = Gson().toJson(Response(msg = "SUCCEED",data = UserInfo("xiaobaowei")))
            Thread.sleep(2000)
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message("")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .addHeader("content-type", "application/json")
                .build()
        } else if (chain.request().url().toString().endsWith("getUsers")) {
            val json = Gson().toJson(Response(msg = "SUCCEED",data = listOf(
                UserInfo("xiaobaowei1"),
                UserInfo("xiaobaowei2"),
                UserInfo("xiaobaowei3"),
                UserInfo("xiaobaowei4"),
                UserInfo("xiaobaowei5"),
                UserInfo("xiaobaowei6"),
            )
            ))
            Thread.sleep(2000)
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message("")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            return chain.proceed(chain.request())
        }
    }
}





