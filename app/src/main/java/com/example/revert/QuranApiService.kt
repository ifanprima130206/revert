package com.example.revert

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuranApiService {

    @GET("surat")
    fun getSurahList(): Call<QuranResponse>

    @GET("surat/{surahNumber}")
    fun getSurahDetail(@Path("surahNumber") surahNumber: Int): Call<SurahDetailResponse>

    companion object {
        private const val BASE_URL = "https://equran.id/api/v2/"

        fun create(): QuranApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuranApiService::class.java)
        }
    }
}
