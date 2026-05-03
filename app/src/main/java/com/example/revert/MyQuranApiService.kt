package com.example.revert

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MyQuranApiService {

    @GET("sholat/jadwal/38af86134b65d0f10fe33d30dd76442e/today")
    fun getJadwalSholat(@Query("tz") tz: String = "Asia/Jakarta"): Call<SholatResponse>

    @GET("cal/today")
    fun getHijriDate(@Query("adj") adj: Int = 0, @Query("tz") tz: String = "Asia/Jakarta"): Call<CalResponse>

    companion object {
        private const val BASE_URL = "https://api.myquran.com/v3/"

        fun create(): MyQuranApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyQuranApiService::class.java)
        }
    }
}
