package com.example.revert

import com.google.gson.annotations.SerializedName

data class Surah(
    @SerializedName("nomor") val number: Int,
    @SerializedName("nama") val name: String,
    @SerializedName("namaLatin") val nameLatin: String,
    @SerializedName("arti") val translation: String,
    @SerializedName("jumlahAyat") val numberOfAyahs: Int,
    @SerializedName("tempatTurun") val revelation: String,
    @SerializedName("ayat") val ayahs: List<Ayah>? = null
)

data class QuranResponse(
    @SerializedName("data") val data: List<Surah>
)

data class SurahDetailResponse(
    @SerializedName("data") val data: Surah
)
