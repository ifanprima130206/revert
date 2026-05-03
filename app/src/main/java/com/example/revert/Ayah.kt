package com.example.revert

import com.google.gson.annotations.SerializedName

data class Ayah(
    @SerializedName("nomorAyat") val ayahNumber: Int,
    @SerializedName("teksArab") val arab: String,
    @SerializedName("teksLatin") val latin: String,
    @SerializedName("teksIndonesia") val translation: String
)
