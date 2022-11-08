package com.wc.cleanmvvm

import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ProjectName:  LeadingEdge
 * Desc:
 * Author: weicheng
 * Date:  2022/11/1
 */



object RetrofitClient {

    val okClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(MockInterceptor()) //造假数据
            .build()
    }

    val retrofitClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com/")
            .client(okClient)
            .build()
    }

}

