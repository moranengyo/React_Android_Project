package com.example.kotiln_tpj_yesim.Retrofit

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
//    private val client = OkHttpClient.Builder()
//        .addInterceptor(interceptor =  {
//            val req = it.request().newBuilder()
//                .addHeader("Authorization", "Bearer " + "myToken")
//                .build()
//
//            return@addInterceptor it.proceed(req)
//        }).build()

    val gson= GsonBuilder().setLenient().create()

    val retrofit :RetrofitInterface = Retrofit.Builder()
//        .client(client)
        .baseUrl("http://10.100.105.240:8080")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(RetrofitInterface::class.java)


}